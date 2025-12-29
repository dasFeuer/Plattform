package com.project.unknown.service;

import com.project.unknown.domain.PagedResponse;
import com.project.unknown.domain.dtos.commentDto.CommentDto;
import com.project.unknown.domain.dtos.commentDto.CreateCommentRequestDto;
import com.project.unknown.domain.dtos.commentDto.UpdateCommentRequestDto;
import com.project.unknown.domain.entities.commentEntity.Comment;

import java.util.Optional;

public interface CommentService {

    CommentDto createComment(CreateCommentRequestDto requestDto, String authorEmail);
    CommentDto getCommentById(Long id);
    PagedResponse<CommentDto> getCommentsByPostId(Long postId, int page, int size);
    PagedResponse<CommentDto> getCommentsByAuthorId(Long authorId, int page, int size);
    CommentDto updateComment(Long id, UpdateCommentRequestDto requestDto, String authorEmail);
    void deleteComment(Long id, String authorEmail);

    Optional<Comment> findCommentById(Long id);
    Comment getCommentEntityById(Long id);
}
