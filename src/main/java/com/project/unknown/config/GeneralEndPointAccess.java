package com.project.unknown.config;

import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.service.PostService;
import com.project.unknown.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class GeneralEndPointAccess {

    private final UserService userService;
    private final PostService postService;

    public Optional<User> getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("No authenticated user found in security context");
            return Optional.empty();
        }

        String email = authentication.getName();
        log.debug("Fetching authenticated user with email: {}", email);

        return userService.findUserByEmail(email);
    }


    public User getAuthenticatedUserOrThrow() {
        return getAuthenticatedUser()
                .orElseThrow(() -> {
                    log.error("User not authenticated");
                    return new ResponseStatusException(
                            HttpStatus.UNAUTHORIZED,
                            "User not authenticated"
                    );
                });
    }

    public void validateUserAccess(Long userId) {
        User authenticatedUser = getAuthenticatedUserOrThrow();

        if (!authenticatedUser.getId().equals(userId)) {
            log.warn("Access denied: User {} tried to access resources of user {}",
                    authenticatedUser.getId(), userId);
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Access denied: You can only modify your own data"
            );
        }

        log.debug("Access validated for user ID: {}", userId);
    }

//    public boolean isPostOwnedByAuthor(Long postId, User user) {
//        validateUserAccess(user.getId());
//
//        return postService.findPostById(postId)
//                .map(post -> {
//                    boolean isOwner = post.getAuthor().getId().equals(user.getId());
//                    log.debug("Post {} ownership check for user {}: {}",
//                            postId, user.getId(), isOwner);
//                    return isOwner;
//                })
//                .orElse(false);
//    }


}
