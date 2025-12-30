package com.project.unknown.controller;

import com.project.unknown.config.GeneralEndPointAccess;
import com.project.unknown.domain.dtos.userDto.*;
import com.project.unknown.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final GeneralEndPointAccess endPointAccess;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        log.info("GET request received for user with ID: {}", id);
        UserResponseDto user = userService.getUserById(id);
        log.debug("Successfully retrieved user with ID: {}", id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserProfileDto>> getAllUsers() {
        log.info("GET request received for all users");
        List<UserProfileDto> users = userService.getAllUsers();
        log.debug("Successfully retrieved {} users", users.size());
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequestDto requestDto) {

        log.info("PUT request received to update user with ID: {}", id);

        endPointAccess.validateUserAccess(id);
        log.debug("User access validated for ID: {}", id);

        UserResponseDto updatedUser = userService.updateUser(id, requestDto);
        log.info("User successfully updated with ID: {}", id);

        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        log.info("DELETE request received for user with ID: {}", id);

        endPointAccess.validateUserAccess(id);
        log.debug("User access validated for ID: {}", id);

        userService.deleteUser(id);
        log.info("User successfully deleted with ID: {}", id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long id) {
        log.info("GET request for user profile with ID: {}", id);
        UserProfileDto profile = userService.getUserProfile(id);
        return ResponseEntity.ok(profile);
    }
}
