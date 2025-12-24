package com.project.unknown.config;

import com.project.unknown.domain.entities.userEntity.User;
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


    private Optional<User> getAuthenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        assert authentication != null;
        String email = authentication.getName();
        return userService.getUserByEmail(email);
    }

    private User getAuthenticatedUserOrThrow() {
        return getAuthenticatedUser()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated"));
    }

    public void validateUserAccess(Long userId) {
        User user = getAuthenticatedUserOrThrow();
        if(!user.getId().equals(userId)){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
    }

}
