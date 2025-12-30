package com.project.unknown.controller;

import com.project.unknown.domain.PagedResponse;
import com.project.unknown.domain.dtos.commentDto.CommentDto;
import com.project.unknown.domain.dtos.commentDto.CreateCommentRequestDto;
import com.project.unknown.domain.dtos.commentDto.UpdateCommentRequestDto;
import com.project.unknown.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
@Slf4j
public class CommentController {
    private final CommentService commentService;

    // EndPoint für die Kommentarerstellung
    @PostMapping
    public ResponseEntity<CommentDto> createComment(
            @Valid @RequestBody CreateCommentRequestDto requestDto,
            Authentication authentication) {

        log.info("POST request to create comment for post ID: '{}'", requestDto.getPostId());

        String authorEmail = authentication.getName();
        CommentDto createdComment = commentService.createComment(requestDto, authorEmail);

        log.info("Post created successfully with ID: {}", createdComment.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdComment);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommentDto> getCommentById(@PathVariable Long id) {
        log.info("GET request for comment with ID: {}", id);

        CommentDto comment = commentService.getCommentById(id);

        return ResponseEntity.ok(comment);
    }

    @GetMapping("/post/{postId}")
    public ResponseEntity<PagedResponse<CommentDto>> getCommentByPost(
            @PathVariable Long postId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET request for comments of post ID: {} - page: {}, size: {}", postId, page, size);

        PagedResponse<CommentDto> comments = commentService.getCommentsByPostId(postId, page, size);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<PagedResponse<CommentDto>> getCommentsByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("GET request for comments of post ID: {} - page: {}, size: {}", authorId, page, size);

        PagedResponse<CommentDto> comments = commentService.getCommentsByAuthorId(authorId, page, size);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentDto> updateComment(
            @PathVariable Long id,
            @Valid @RequestBody UpdateCommentRequestDto requestDto,
            Authentication authentication) {

        log.info("PUT request to update comment with ID: {}", id);

        String authorEmail = authentication.getName();
        CommentDto updatedComment = commentService.updateComment(id, requestDto, authorEmail);

        return ResponseEntity.ok(updatedComment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long id,
            Authentication authentication) {

        log.info("DELETE request for comment with ID: {}", id);

        String authorEmail = authentication.getName();
        commentService.deleteComment(id, authorEmail);

        return ResponseEntity.noContent().build();
    }
}
