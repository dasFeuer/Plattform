package com.project.unknown.service;

import com.project.unknown.domain.dtos.userDto.RegisterUserRequestDto;
import com.project.unknown.domain.dtos.userDto.UpdateUserRequestDto;
import com.project.unknown.domain.dtos.userDto.UserProfileDto;
import com.project.unknown.domain.dtos.userDto.UserResponseDto;
import com.project.unknown.domain.entities.userEntity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    UserResponseDto registerUser(RegisterUserRequestDto requestDto);

    // Basic CRUD
    UserResponseDto getUserById(Long id);
    List<UserResponseDto> getAllUsers();
    UserResponseDto updateUser(Long id, UpdateUserRequestDto requestDto);
    void deleteUser(Long id);

    // Profile with Stats
    UserProfileDto getUserProfile(Long id);

    // Profile Image
    UserResponseDto updateProfileImage(Long userId, String imagePath);
    void deleteProfileImage(Long userId);

    // Internal Methods
    User getUserEntityById(Long id);
    User getUserEntityByEmail(String email);
    Optional<User> findUserByEmail(String email);
}
