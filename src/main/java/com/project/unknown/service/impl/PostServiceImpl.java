package com.project.unknown.service.impl;

import com.project.unknown.domain.CreatePostRequest;
import com.project.unknown.domain.UpdatePostRequest;
import com.project.unknown.domain.entities.postEntity.Post;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.repository.PostRepository;
import com.project.unknown.repository.UserRepository;
import com.project.unknown.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public Optional<Post> findPostById(Long id) {
            return postRepository.findById(id);
    }

    @Override
    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post mit " + id + " nicht gefunden"));
    }

    @Override
    public void deletePostById(Long id) {
        if(postRepository.existsById(id)) {
            postRepository.deleteById(id);
        } else {
            throw new RuntimeException("Post mit " + id + " nicht gefunden");
        }
    }

//    @Override
//    public void deletePostById(Long id) {
//        if(!postRepository.existsById(id)) {
//            throw new RuntimeException("Post mit " + id + " nicht gefunden");
//        }
//        postRepository.deleteById(id);
//    }

    @Override
    public Post updatePostById(Long id, UpdatePostRequest updatePostRequest) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Post nicht gefunden"));
        existingPost.setTitle(updatePostRequest.getTitle());
        existingPost.setContent(updatePostRequest.getContent());
        return postRepository.save(existingPost);
    }
}
