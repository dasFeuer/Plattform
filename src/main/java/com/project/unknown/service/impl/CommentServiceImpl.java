package com.project.unknown.service.impl;

import com.project.unknown.domain.PagedResponse;
import com.project.unknown.domain.dtos.commentDto.CommentDto;
import com.project.unknown.domain.dtos.commentDto.CreateCommentRequestDto;
import com.project.unknown.domain.dtos.commentDto.UpdateCommentRequestDto;
import com.project.unknown.domain.entities.commentEntity.Comment;
import com.project.unknown.domain.entities.postEntity.Post;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.exception.ResourceNotFoundException;
import com.project.unknown.mapper.CommentMapper;
import com.project.unknown.repository.CommentRepository;
import com.project.unknown.service.CommentService;
import com.project.unknown.service.PostService;
import com.project.unknown.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final UserService userService;
    private final PostService postService;

    @Override
    public CommentDto createComment(CreateCommentRequestDto requestDto, String authorEmail) {
        log.info("Creating comment for post ID: {} by author: {}", requestDto.getPostId(), authorEmail);
        User author = userService.getUserEntityByEmail(authorEmail);

        Post post = postService.getPostEntityById(requestDto.getPostId());

        Comment comment = Comment.builder()
                .content(requestDto.getContent())
                .post(post)
                .author(author)
                .reactions(new ArrayList<>())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Comment savedComment = commentRepository.save(comment);
        log.info("Comment created successfully with ID: {}", savedComment.getId());

        return commentMapper.toDto(savedComment);
    }

    @Override
    public CommentDto getCommentById(Long id) {
        log.debug("Fetching comment DTO for ID: {}", id);
        Comment comment = getCommentEntityById(id);
        return commentMapper.toDto(comment);
    }

    @Override
    public PagedResponse<CommentDto> getCommentsByPostId(Long postId, int page, int size) {
        log.info("Fetching comments for post ID: {} - page: {}, size: {}", postId, page, size);

        postService.getPostEntityById(postId);

        PageRequest pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> commentsPage = commentRepository.findByPostIdOrderByCreatedAtDesc(postId, pageable);

        List<CommentDto> commentDtos = commentMapper.toDtoList(commentsPage.getContent());

        PagedResponse<CommentDto> response = PagedResponse.<CommentDto>builder()
                .content(commentDtos)
                .pageNumber(commentsPage.getNumber())
                .pageSize(commentsPage.getSize())
                .totalElements(commentsPage.getTotalElements())
                .totalPages(commentsPage.getTotalPages())
                .last(commentsPage.isLast())
                .first(commentsPage.isFirst())
                .build();

        log.debug("Post {} has {} comments total", postId, commentsPage.getTotalElements());

        return response;

    }


    public PagedResponse<CommentDto> getCommentsByAuthorId(Long authorId, int page, int size) {
        log.info("Fetching comments for author ID: {} - page: {}, size: {}", authorId, page, size);

        userService.getUserEntityById(authorId);

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Comment> commentsPage = commentRepository.findByAuthorIdOrderByCreatedAtDesc(authorId, pageable);

        List<CommentDto> commentDtos = commentMapper.toDtoList(commentsPage.getContent());

        PagedResponse<CommentDto> response = PagedResponse.<CommentDto>builder()
                .content(commentDtos)
                .pageNumber(commentsPage.getNumber())
                .pageSize(commentsPage.getSize())
                .totalElements(commentsPage.getTotalElements())
                .totalPages(commentsPage.getTotalPages())
                .last(commentsPage.isLast())
                .first(commentsPage.isFirst())
                .build();

        log.debug("Author {} has {} comments total", authorId, commentsPage.getTotalElements());

        return response;
    }

    @Override
    public CommentDto updateComment(Long id, UpdateCommentRequestDto requestDto, String authorEmail) {
        log.info("Updating comment with ID: {}", id);
        Comment comment = getCommentEntityById(id);
        User author = userService.getUserEntityByEmail(authorEmail);

        if (!comment.getAuthor().getId().equals(author.getId())) {
            log.warn("User {} tried to update comment {} owned by {}",
                    author.getId(), id, comment.getAuthor().getId());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only update your own comments"
            );
        }

        comment.setContent(requestDto.getContent());
        comment.setUpdatedAt(LocalDateTime.now());

        Comment updatedComment  = commentRepository.save(comment);
        log.info("Comment {} updated successfully", id);
        return commentMapper.toDto(updatedComment );
    }

    @Override
    @Transactional
    public void deleteComment(Long id, String authorEmail) {
        log.info("Deleting comment with ID: {}", id);

        Comment comment = getCommentEntityById(id);

        User author = userService.getUserEntityByEmail(authorEmail);
        if (!comment.getAuthor().getId().equals(author.getId())) {
            log.warn("User {} tried to delete comment {} owned by {}",
                    author.getId(), id, comment.getAuthor().getId());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only delete your own comments"
            );
        }

        commentRepository.deleteById(id);
        log.info("Comment {} deleted successfully", id);
    }

    @Override
    public Optional<Comment> findCommentById(Long id) {
        log.debug("Finding comment by ID: {}", id);
        return commentRepository.findById(id);    }

    @Override
    public Comment getCommentEntityById(Long id) {
        log.debug("Fetching comment entity for ID: {}", id);
        return commentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Comment with ID {} not found", id);
                    return new ResourceNotFoundException("Comment with ID " + id + " not found");
                });
    }
}
