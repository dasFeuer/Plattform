package com.project.unknown.controller;

import com.project.unknown.domain.PagedResponse;
import com.project.unknown.domain.dtos.mediaDto.MediaDto;
import com.project.unknown.domain.dtos.postDto.*;
import com.project.unknown.service.impl.FileStorageServiceImpl;
import com.project.unknown.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Slf4j
public class PostController {
    private final PostService postService;
    private final FileStorageServiceImpl fileStorageService;

    @PostMapping
    public ResponseEntity<PostDetailDto> createPost(
            @Valid @RequestBody CreatePostRequestDto requestDto,
            Authentication authentication) {

        log.info("POST request to create post with title: '{}'", requestDto.getTitle());

        String authorEmail = authentication.getName();
        PostDetailDto createdPost = postService.createPost(requestDto, authorEmail);

        log.info("Post created successfully with ID: {}", createdPost.getId());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdPost);
    }


    // GET /api/posts?page=0&size=10&sort=createdAt,desc
    @GetMapping
    public ResponseEntity<PagedResponse<PostSummaryDto>> getAllPosts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort) {

        log.info("GET request for all posts - page: {}, size: {}", page, size);

        PagedResponse<PostSummaryDto> posts = postService.getAllPosts(page, size, sort);

        log.debug("Returning {} posts on page {}", posts.getContent().size(), page);

        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostDetailDto> getPostById(@PathVariable Long id) {
        log.info("GET request for post with ID: {}", id);

        PostDetailDto post = postService.getPostById(id);

        log.debug("Successfully retrieved post with ID: {}", id);

        return ResponseEntity.ok(post);
    }

    @GetMapping("/author/{authorId}")
    public ResponseEntity<PagedResponse<PostSummaryDto>> getPostsByAuthor(
            @PathVariable Long authorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET request for posts by author ID: {} - page: {}, size: {}", authorId, page, size);

        PagedResponse<PostSummaryDto> posts = postService.getPostsByAuthorId(authorId, page, size);

        log.debug("Returning {} posts for author {}", posts.getContent().size(), authorId);

        return ResponseEntity.ok(posts);
    }


    // GET /api/posts/search?keyword=spring&page=0&size=10
    @GetMapping("/search")
    public ResponseEntity<PagedResponse<PostSummaryDto>> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        log.info("GET request to search posts with keyword: '{}' - page: {}, size: {}", keyword, page, size);

        PagedResponse<PostSummaryDto> posts = postService.searchPostsByTitle(keyword, page, size);

        log.debug("Found {} posts matching keyword '{}'", posts.getTotalElements(), keyword);

        return ResponseEntity.ok(posts);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostDetailDto> updatePost(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePostRequestDto requestDto,
            Authentication authentication) {

        log.info("PUT request to update post with ID: {}", id);

        String authorEmail = authentication.getName();
        PostDetailDto updatedPost = postService.updatePost(id, requestDto, authorEmail);

        log.info("Post {} updated successfully", id);

        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            Authentication authentication) {

        log.info("DELETE request for post with ID: {}", id);

        String authorEmail = authentication.getName();
        postService.deletePost(id, authorEmail);

        log.info("Post {} deleted successfully", id);

        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/{postId}/media", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<MediaDto>> uploadPostMedia(
            @PathVariable Long postId,
            @RequestParam("files") MultipartFile[] files,
            Authentication authentication) {

        log.info("Upload media request for post ID: {}", postId);

        try {
            List<MediaDto> uploadedMedia = postService.addMediaToPost(postId, files, authentication.getName());
            return ResponseEntity.ok(uploadedMedia);
        } catch (IllegalArgumentException e) {
            log.error("Invalid files: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/media/{mediaId}")
    public ResponseEntity<Void> deleteMedia(
            @PathVariable Long mediaId,
            Authentication authentication) {

        log.info("Delete media request for media ID: {}", mediaId);
        postService.deleteMedia(mediaId, authentication.getName());
        return ResponseEntity.noContent().build();
    }
}
