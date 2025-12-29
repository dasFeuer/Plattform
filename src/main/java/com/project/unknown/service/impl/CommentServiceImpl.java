package com.project.unknown.service.impl;

import com.project.unknown.domain.PagedResponse;
import com.project.unknown.domain.dtos.commentDto.CommentDto;
import com.project.unknown.domain.dtos.commentDto.CreateCommentRequestDto;
import com.project.unknown.domain.dtos.commentDto.UpdateCommentRequestDto;
import com.project.unknown.domain.entities.commentEntity.Comment;
import com.project.unknown.domain.entities.postEntity.Post;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.mapper.CommentMapper;
import com.project.unknown.repository.CommentRepository;
import com.project.unknown.service.CommentService;
import com.project.unknown.service.PostService;
import com.project.unknown.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        return null;
    }

    @Override
    public PagedResponse<CommentDto> getCommentsByPostId(Long postId, int page, int size) {
        return null;
    }

    @Override
    public PagedResponse<CommentDto> getCommentsByAuthorId(Long authorId, int page, int size) {
        return null;
    }

    @Override
    public CommentDto updateComment(Long id, UpdateCommentRequestDto requestDto, String authorEmail) {
        return null;
    }

    @Override
    public void deleteComment(Long id, String authorEmail) {

    }

    @Override
    public Optional<Comment> findCommentById(Long id) {
        return Optional.empty();
    }

    @Override
    public Comment getCommentEntityById(Long id) {
        return null;
    }
}
