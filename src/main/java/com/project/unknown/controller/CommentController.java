package com.project.unknown.controller;

import com.project.unknown.domain.dtos.commentDto.CommentDto;
import com.project.unknown.domain.dtos.commentDto.CreateCommentRequestDto;
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
}
