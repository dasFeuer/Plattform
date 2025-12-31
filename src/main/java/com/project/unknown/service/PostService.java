package com.project.unknown.service;

import com.project.unknown.domain.PagedResponse;
import com.project.unknown.domain.dtos.mediaDto.MediaDto;
import com.project.unknown.domain.dtos.postDto.CreatePostRequestDto;
import com.project.unknown.domain.dtos.postDto.PostDetailDto;
import com.project.unknown.domain.dtos.postDto.PostSummaryDto;
import com.project.unknown.domain.dtos.postDto.UpdatePostRequestDto;
import com.project.unknown.domain.entities.postEntity.Post;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
    public List<MediaDto> addMediaToPost(Long postId, MultipartFile[] files, String authorEmail);
    public void deleteMedia(Long mediaId, String authorEmail);

    // Interne Methoden- geben Entity zurück)
    Optional<Post> findPostById(Long id);
    Post getPostEntityById(Long id);
}
