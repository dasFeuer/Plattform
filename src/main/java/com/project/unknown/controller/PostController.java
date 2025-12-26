package com.project.unknown.controller;

import com.project.unknown.config.GeneralEndPointAccess;
import com.project.unknown.domain.CreatePostRequest;
import com.project.unknown.domain.UpdatePostRequest;
import com.project.unknown.domain.dtos.postDto.CreatePostRequestDto;
import com.project.unknown.domain.dtos.postDto.PostDto;
import com.project.unknown.domain.dtos.postDto.UpdatePostRequestDto;
import com.project.unknown.domain.entities.postEntity.Post;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.mapper.PostMapper;
import com.project.unknown.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostMapper postMapper;

    private final PostService postService;

    private final GeneralEndPointAccess endPointAccess;

    @PostMapping("/{userId}/create-post")
    public ResponseEntity<PostDto> createPost(@PathVariable Long userId,
                                              @RequestBody CreatePostRequestDto createPostRequestDto){
        CreatePostRequest createPost = postMapper.toCreatePost(createPostRequestDto);
        Post post = postService.createPost(userId, createPost);
        PostDto dto = postMapper.toDto(post);
        return new ResponseEntity<>(dto, HttpStatus.CREATED);
    }


    @PutMapping("/{postId}/update-post")
    public ResponseEntity<PostDto> updatePost(@PathVariable Long postId,
                                              @RequestBody UpdatePostRequestDto updatePostRequestDto){

        Optional<User> authenticatedUser = endPointAccess.getAuthenticatedUser();
        if (authenticatedUser.isPresent()){
            if(endPointAccess.isPostOwnedByAuthor(postId, authenticatedUser.get())){
                UpdatePostRequest updatePost = postMapper.toUpdatePost(updatePostRequestDto);
                Post post = postService.updatePostById(postId, updatePost);
                PostDto dto = postMapper.toDto(post);
                return new ResponseEntity<>(dto, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{postId}/get-post")
    public ResponseEntity<PostDto> getPostById(@PathVariable Long postId){
        Post post = postService.getPostById(postId);
            PostDto dto = postMapper.toDto(post);
            return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/getAllPosts")
    public ResponseEntity<List<PostDto>> getPostById(){
        List<Post> allPosts = postService.getAllPosts();
        List<PostDto> list = allPosts
                .stream()
                .map(postMapper::toDto)
                .toList();
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @DeleteMapping("/{postId}/delete-post")
    public ResponseEntity<Void> deletePostById(@PathVariable Long postId){
        Optional<User> authenticatedUser = endPointAccess.getAuthenticatedUser();
        if (authenticatedUser.isPresent()){
            if(endPointAccess.isPostOwnedByAuthor(postId, authenticatedUser.get())) {
                postService.deletePostById(postId);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
