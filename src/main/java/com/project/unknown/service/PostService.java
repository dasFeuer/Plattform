package com.project.unknown.service;

import com.project.unknown.domain.CreatePostRequest;
import com.project.unknown.domain.UpdatePostRequest;
import com.project.unknown.domain.entities.postEntity.Post;

import java.util.List;
import java.util.Optional;

public interface PostService {
    Post createPost (Long userId, CreatePostRequest createPostRequest);
    List<Post> getAllPosts();
    Optional<Post> findPostById(Long id);
    Post getPostById(Long id);
    void deletePostById(Long id);
    Post updatePostById(Long id, UpdatePostRequest updatePostRequest);
}
