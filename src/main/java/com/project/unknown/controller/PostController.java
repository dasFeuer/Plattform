package com.project.unknown.controller;

import com.project.unknown.domain.CreatePostRequest;
import com.project.unknown.domain.dtos.postDto.CreatePostRequestDto;
import com.project.unknown.domain.dtos.postDto.PostDto;
import com.project.unknown.domain.entities.postEntity.Post;
import com.project.unknown.mapper.PostMapper;
import com.project.unknown.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostMapper postMapper;

    private final PostService postService;

    @PostMapping("/{id}/create-post")
    public ResponseEntity<PostDto> createPost(@PathVariable Long userId,
                                              @RequestBody CreatePostRequestDto createPostRequestDto){
        CreatePostRequest createPost = postMapper.toCreatePost(createPostRequestDto);
        Post post = postService.createPost(userId, createPost);
        PostDto dto = postMapper.toDto(post);
        return ResponseEntity.ok(dto);

    }
}
