package com.project.unknown.service.impl;


import com.project.unknown.domain.dtos.userDto.RegisterUserRequestDto;
import com.project.unknown.domain.dtos.userDto.UpdateUserRequestDto;
import com.project.unknown.domain.dtos.userDto.UserProfileDto;
import com.project.unknown.domain.dtos.userDto.UserResponseDto;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.exception.DuplicateResourceException;
import com.project.unknown.exception.ResourceNotFoundException;
import com.project.unknown.mapper.UserMapper;
import com.project.unknown.repository.CommentRepository;
import com.project.unknown.repository.PostRepository;
import com.project.unknown.repository.ReactionRepository;
import com.project.unknown.repository.UserRepository;
import com.project.unknown.service.EmailVerificationTokenService;
import com.project.unknown.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final EmailVerificationTokenService emailVerificationTokenService;
        private final UserMapper userMapper;
        private final PostRepository postRepository;
        private final CommentRepository commentRepository;
        private final ReactionRepository reactionRepository;
        private final FileStorageServiceImpl fileStorageService;

        @Override
        @Transactional
        public UserResponseDto registerUser(RegisterUserRequestDto requestDto) {
            log.info("Attempting to register new user with username: {}", requestDto.getUsername());

            if (userRepository.existsByUsername(requestDto.getUsername())) {
                log.warn("Registration failed: Username '{}' is already taken", requestDto.getUsername());
                throw new DuplicateResourceException(
                        "Username '" + requestDto.getUsername() + "' is already taken"
                );
            }

            if (userRepository.existsByEmail(requestDto.getEmail())) {
                log.warn("Registration failed: Email '{}' is already registered", requestDto.getEmail());
                throw new DuplicateResourceException(
                        "Email '" + requestDto.getEmail() + "' is already registered"
                );
            }

            User newUser = User.builder()
                    .firstName(requestDto.getFirstName())
                    .lastName(requestDto.getLastName())
                    .username(requestDto.getUsername())
                    .email(requestDto.getEmail())
                    .password(passwordEncoder.encode(requestDto.getPassword()))
                    .verified(false)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            User savedUser = userRepository.save(newUser);
            log.info("User successfully registered with ID: {} and username: {}",
                    savedUser.getId(), savedUser.getUsername());

            try {
                emailVerificationTokenService.sendVerificationEmail(savedUser);
                log.info("Verification email sent to: {}", savedUser.getEmail());
            } catch (Exception e) {
                log.error("Failed to send verification email to: {}", savedUser.getEmail(), e);
            }

            return userMapper.toResponseDto(savedUser);
        }


        @Override
        public UserResponseDto getUserById(Long id) {
            log.debug("Fetching user DTO for ID: {}", id);
            User user = getUserEntityById(id);
            return userMapper.toResponseDto(user);
        }


    @Override
    public UserProfileDto getUserProfile(Long id) {
        log.debug("Fetching user profile for ID: {}", id);

        User user = getUserEntityById(id);

        long totalPosts = postRepository.countByAuthorId(id);
        long totalComments = commentRepository.countByAuthorId(id);
        long totalReactions = reactionRepository.countByUserId(id);

        UserProfileDto profileDto = UserProfileDto.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .email(user.getEmail())
                .verified(user.isVerified())
                .createdAt(user.getCreatedAt())
                .profileImagePath(user.getProfileImagePath())
                .totalPosts(totalPosts)
                .totalComments(totalComments)
                .totalReactions(totalReactions)
                .build();

        log.debug("User profile stats - Posts: {}, Comments: {}, Reactions: {}",
                totalPosts, totalComments, totalReactions);

        return profileDto;
    }


    @Override
    public List<UserResponseDto> getAllUsers() {
        log.debug("Fetching all users");
        List<User> users = userRepository.findAll();
        return userMapper.toResponseDtoList(users);
    }

//    @Override
//    public List<UserProfileDto> getAllUsers() {
//        log.debug("Fetching all users with stats");
//
//        List<User> users = userRepository.findAll();
//
//        // Für jeden User Stats berechnen
//        return users.stream()
//                .map(user -> {
//                    long totalPosts = postRepository.countByAuthorId(user.getId());
//                    long totalComments = commentRepository.countByAuthorId(user.getId());
//                    long totalReactions = reactionRepository.countByUserId(user.getId());
//
//                    return UserProfileDto.builder()
//                            .id(user.getId())
//                            .firstName(user.getFirstName())
//                            .lastName(user.getLastName())
//                            .username(user.getUsername())
//                            .email(user.getEmail())
//                            .verified(user.isVerified())
//                            .createdAt(user.getCreatedAt())
//                            .profileImagePath(user.getProfileImagePath())
//                            .totalPosts(totalPosts)
//                            .totalComments(totalComments)
//                            .totalReactions(totalReactions)
//                            .build();
//                })
//                .collect(Collectors.toList());
//    }

        @Override
        @Transactional
        public UserResponseDto updateUser(Long id, UpdateUserRequestDto requestDto) {
            log.info("Attempting to update user with ID: {}", id);

            User user = getUserEntityById(id);

            if (!user.getUsername().equals(requestDto.getUsername())) {
                if (userRepository.existsByUsername(requestDto.getUsername())) {
                    log.warn("Update failed: Username '{}' is already taken", requestDto.getUsername());
                    throw new DuplicateResourceException(
                            "Username '" + requestDto.getUsername() + "' is already taken"
                    );
                }
                log.debug("Username will be changed from '{}' to '{}'",
                        user.getUsername(), requestDto.getUsername());
            }

            if (!user.getEmail().equals(requestDto.getEmail())) {
                if (userRepository.existsByEmail(requestDto.getEmail())) {
                    log.warn("Update failed: Email '{}' is already registered", requestDto.getEmail());
                    throw new DuplicateResourceException(
                            "Email '" + requestDto.getEmail() + "' is already registered"
                    );
                }
                log.debug("Email will be changed from '{}' to '{}'",
                        user.getEmail(), requestDto.getEmail());
            }

            user.setFirstName(requestDto.getFirstName());
            user.setLastName(requestDto.getLastName());
            user.setUsername(requestDto.getUsername());
            user.setEmail(requestDto.getEmail());

            if (requestDto.getPassword() != null && !requestDto.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
                log.debug("Password will be updated for user ID: {}", id);
            }

            user.setUpdatedAt(LocalDateTime.now());

            User updatedUser = userRepository.save(user);
            log.info("User successfully updated with ID: {}", updatedUser.getId());

            return userMapper.toResponseDto(updatedUser);
        }


        @Override
        @Transactional
        public void deleteUser(Long id) {
            log.info("Attempting to delete user with ID: {}", id);

            if (!userRepository.existsById(id)) {
                log.warn("Delete failed: User with ID {} not found", id);
                throw new ResourceNotFoundException("User with ID " + id + " not found");
            }

            userRepository.deleteById(id);
            log.info("User successfully deleted with ID: {}", id);
        }


    @Override
    @Transactional
    public UserResponseDto updateProfileImage(Long userId, String imagePath) {
        log.info("Updating profile image for user ID: {}", userId);

        User user = getUserEntityById(userId);

        // Altes Bild löschen
        if (user.getProfileImagePath() != null) {
            fileStorageService.deleteFile(user.getProfileImagePath());
        }

        // Neues Bild setzen
        user.setProfileImagePath(imagePath);
        User updatedUser = userRepository.save(user);

        log.info("Profile image updated for user ID: {}", userId);
        return userMapper.toResponseDto(updatedUser);
    }

    @Override
    @Transactional
    public void deleteProfileImage(Long userId) {
        log.info("Deleting profile image for user ID: {}", userId);

        User user = getUserEntityById(userId);

        if (user.getProfileImagePath() != null) {
            fileStorageService.deleteFile(user.getProfileImagePath());
            user.setProfileImagePath(null);
            userRepository.save(user);
        }
    }

        // INTERNE METHODEN
        // Nur von anderen Services/Components verwendet!

        @Override
        public User getUserEntityById(Long id) {
            log.debug("Fetching user entity for ID: {}", id);
            return userRepository.findById(id)
                    .orElseThrow(() -> {
                        log.error("User with ID {} not found", id);
                        return new ResourceNotFoundException("User with ID " + id + " not found");
                    });
        }

        @Override
        public User getUserEntityByEmail(String email) {
            log.debug("Fetching user entity for email: {}", email);
            return userRepository.findByEmail(email)
                    .orElseThrow(() -> {
                        log.error("User with email {} not found", email);
                        return new ResourceNotFoundException("User with email " + email + " not found");
                    });
        }

        @Override
        public Optional<User> findUserByEmail(String email) {
            log.debug("Finding user by email: {}", email);
            return userRepository.findByEmail(email);
        }
}
