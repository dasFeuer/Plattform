package com.project.unknown.service;

import com.project.unknown.domain.PagedResponse;
import com.project.unknown.domain.dtos.postDto.CreatePostRequestDto;
import com.project.unknown.domain.dtos.postDto.PostDetailDto;
import com.project.unknown.domain.dtos.postDto.PostSummaryDto;
import com.project.unknown.domain.dtos.postDto.UpdatePostRequestDto;
import com.project.unknown.domain.entities.postEntity.Post;

import java.util.Optional;

public interface PostService {

    // API Methoden (geben DTO zurück)
    PostDetailDto createPost(CreatePostRequestDto requestDto, String authorEmail);
    PostDetailDto getPostById(Long id);
    PagedResponse<PostSummaryDto> getAllPosts(int page, int size, String[] sort);
    PagedResponse<PostSummaryDto> getPostsByAuthorId(Long authorId, int page, int size);
    PagedResponse<PostSummaryDto> searchPostsByTitle(String keyword, int page, int size);
    PostDetailDto updatePost(Long id, UpdatePostRequestDto requestDto, String authorEmail);
    void deletePost(Long id, String authorEmail);

    // Interne Methoden- geben Entity zurück)
    Optional<Post> findPostById(Long id);
    Post getPostEntityById(Long id);
}
