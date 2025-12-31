package com.project.unknown.controller;

import com.project.unknown.config.GeneralEndPointAccess;
import com.project.unknown.domain.dtos.userDto.*;
import com.project.unknown.service.impl.FileStorageServiceImpl;
import com.project.unknown.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Slf4j
public class UserController {

    private final UserService userService;
    private final GeneralEndPointAccess endPointAccess;
    private final FileStorageServiceImpl fileStorageServiceImpl;

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id) {
        log.info("GET request received for user with ID: {}", id);
        UserResponseDto user = userService.getUserById(id);
        log.debug("Successfully retrieved user with ID: {}", id);
        return ResponseEntity.ok(user);
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        log.info("GET request for all users");
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@PathVariable Long id) {
        log.info("GET request for user profile with ID: {}", id);
        UserProfileDto profile = userService.getUserProfile(id);
        return ResponseEntity.ok(profile);
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

    @PostMapping(value = "/{id}/profile-image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<UserResponseDto> uploadProfileImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            Authentication authentication) {

        log.info("Upload profile image request for user ID: {}", id);

        // Nur eigenes Bild hochladen
        endPointAccess.validateUserAccess(id);

//        String email = authentication.getName();
//        userService.findUserByEmail(email);
        try {
            String imagePath = fileStorageServiceImpl.saveProfileImage(file, id);
            UserResponseDto updatedUser = userService.updateProfileImage(id, imagePath);
            return ResponseEntity.ok(updatedUser);
        } catch (IllegalArgumentException e) {
            log.error("Invalid file: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}/profile-image")
    public ResponseEntity<Void> deleteProfileImage(
            @PathVariable Long id,
            Authentication authentication) {

        log.info("Delete profile image request for user ID: {}", id);
        endPointAccess.validateUserAccess(id);

        userService.deleteProfileImage(id);
        return ResponseEntity.noContent().build();
    }
}
