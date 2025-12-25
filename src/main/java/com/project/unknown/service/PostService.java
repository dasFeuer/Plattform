package com.project.unknown.service;

import com.project.unknown.domain.CreatePostRequest;
import com.project.unknown.domain.entities.postEntity.Post;

public interface PostService {
    Post createPost (Long userId, CreatePostRequest createPostRequest);
}
