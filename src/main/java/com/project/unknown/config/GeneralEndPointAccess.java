package com.project.unknown.config;

import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.service.PostService;
import com.project.unknown.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GeneralEndPointAccess {

    private final UserService userService;

    private final PostService postService;


    public Optional<User> getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String email = authentication.getName();
        return userService.getUserByEmail(email);
    }

    public User getAuthenticatedUserOrThrow() {
        return getAuthenticatedUser()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated"));
    }

    public void validateUserAccess(Long userId) {
        User user = getAuthenticatedUserOrThrow();
        if(!user.getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
    }

//    public boolean isPostOwnedByAuthor(Long postId, User user){
//        validateUserAccess(user.getId());
//        Optional<Post> post = postService.findPostById(postId);
//        return post.isPresent() && post.get().getAuthor().getId().equals(user.getId());
//    }

    public boolean isPostOwnedByAuthor(Long postId, User user){
        validateUserAccess(user.getId());
        return postService.findPostById(postId)
                .map(post -> post
                        .getAuthor()
                        .getId()
                        .equals(user.getId()))
                .orElse(false);
    }

}
