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
    UserResponseDto getUserById(Long id);
    UserProfileDto getUserProfile(Long id);
    List<UserProfileDto> getAllUsers();
    UserResponseDto updateUser(Long id, UpdateUserRequestDto requestDto);
    void deleteUser(Long id);
    UserResponseDto getUserByEmailDto(String email);

    // Interne Methoden (geben Entity zurück)
    User getUserEntityById(Long id);
    User getUserEntityByEmail(String email);
    Optional<User> findUserByEmail(String email);
}
