package com.project.unknown.service.impl;

import com.project.unknown.domain.PatchUserDataRequest;
import com.project.unknown.domain.RegisterUserRequest;
import com.project.unknown.domain.UpdateUserDataRequest;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.repository.UserRepository;
import com.project.unknown.service.EmailVerificationTokenService;
import com.project.unknown.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailVerificationTokenService emailVerificationTokenService;

    @Override
    public User registerUser(RegisterUserRequest registerUserRequest) {
        if(userRepository.existsByUsername(registerUserRequest.getUsername())){
            throw new IllegalArgumentException("Username is already taken!");
        }
        if(userRepository.existsByEmail(registerUserRequest.getEmail())){
            throw new IllegalArgumentException("Email is already taken!");
        }

        User newUser = new User();
        newUser.setFirstName(registerUserRequest.getFirstName());
        newUser.setLastName(registerUserRequest.getLastName());
        newUser.setUsername(registerUserRequest.getUsername());
        if(registerUserRequest.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")){
            newUser.setEmail(registerUserRequest.getEmail());
        } else {
            throw new IllegalArgumentException("Invalid email address!");

        }
        newUser.setPassword(passwordEncoder.encode(registerUserRequest.getPassword()));
        User savedUser = userRepository.save(newUser);
        emailVerificationTokenService.sendVerificationEmail(savedUser);
        return savedUser;
    }

    @Override
    public Optional<User> getUserById(Long id) {
        if(!userRepository.existsById(id)){
            throw new IllegalArgumentException("User Id " + id + " doesn't found");
        } else {
            return userRepository.findById(id);
        }
    }

    @Override
    public void deleteUserById(Long id) {
        if (userRepository.existsById(id)){
            userRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("User Id " + id + " doesn't found");
        }
    }

    @Override
    public List<User> getAllUser() {
        return userRepository.findAll();
    }

    @Override
    public User patchUserInfo(Long id, PatchUserDataRequest patchUserDataRequest) {
        Optional<User> existingUser = userRepository.findById(id);

        if (existingUser.isPresent()){
            User updateUser = existingUser.get();
            if (patchUserDataRequest.getFirstName() != null){
                updateUser.setFirstName(patchUserDataRequest.getFirstName());
            }
            if (patchUserDataRequest.getLastName() != null){
                updateUser.setLastName(patchUserDataRequest.getLastName());
            }
            if (patchUserDataRequest.getUsername() != null) {
                updateUser.setUsername(patchUserDataRequest.getUsername());
            }
            if (patchUserDataRequest.getEmail() != null) {
                updateUser.setEmail(patchUserDataRequest.getEmail());
            }
            if (patchUserDataRequest.getPassword() != null) {
                updateUser.setPassword(passwordEncoder.encode(patchUserDataRequest.getPassword()));
            }
            return userRepository.save(updateUser);
        } else {
            throw new EntityNotFoundException("User not found!");
        }
    }

    @Override
    public User updateUserInfo(Long id, UpdateUserDataRequest updateUserDataRequest) {
        Optional<User> existingUser = userRepository.findById(id);
        if (existingUser.isPresent()) {
            User updatedUser = existingUser.get();
            updatedUser.setFirstName(updatedUser.getFirstName());
            updatedUser.setLastName(updatedUser.getLastName());
            updatedUser.setUsername(updateUserDataRequest.getUsername());
            updatedUser.setEmail(updateUserDataRequest.getEmail());
            updatedUser.setPassword(passwordEncoder.encode(updateUserDataRequest.getPassword()));
            return userRepository.save(updatedUser);
        } else {
            throw new EntityNotFoundException("User not found!");
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
