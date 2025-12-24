package com.project.unknown.service;

import com.project.unknown.domain.PatchUserDataRequest;
import com.project.unknown.domain.RegisterUserRequest;
import com.project.unknown.domain.UpdateUserDataRequest;
import com.project.unknown.domain.entities.userEntity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User registerUser(RegisterUserRequest registerUserRequest);
    Optional<User> getUserById(Long id);
    void deleteUserById(Long id);
    List<User> getAllUser();
    User patchUserInfo(Long id, PatchUserDataRequest patchUserDataRequest);
    User updateUserInfo(Long id, UpdateUserDataRequest updateUserDataRequest);
    Optional<User> getUserByEmail(String email);
}
