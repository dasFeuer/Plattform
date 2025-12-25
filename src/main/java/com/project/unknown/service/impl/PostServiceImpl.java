package com.project.unknown.service.impl;

import com.project.unknown.domain.CreatePostRequest;
import com.project.unknown.domain.entities.postEntity.Post;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.repository.PostRepository;
import com.project.unknown.repository.UserRepository;
import com.project.unknown.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;

    private final PostRepository postRepository;

    @Override
    public Post createPost(Long userId, CreatePostRequest createPostRequest) {
        User loggedInUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Benutzer nicht gefunden"));
        Post post = new Post();
        post.setTitle(createPostRequest.getTitle());
        post.setContent(createPostRequest.getContent());
        post.setAuthor(loggedInUser);
        return postRepository.save(post);
    }
}
