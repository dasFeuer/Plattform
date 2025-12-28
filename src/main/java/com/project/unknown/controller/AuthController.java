package com.project.unknown.controller;

import com.project.unknown.auth.JwtService;
import com.project.unknown.domain.dtos.userDto.*;
import com.project.unknown.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> registerUser(
            @Valid @RequestBody RegisterUserRequestDto requestDto) {

        log.info("Registration request received for username: {}", requestDto.getUsername());

        UserResponseDto registeredUser = userService.registerUser(requestDto);

        log.info("User successfully registered with username: {}", registeredUser.getUsername());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(registeredUser);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(
            @Valid @RequestBody LoginUserRequestDto requestDto) {

        log.info("Login request received for: {}", requestDto.getLogin());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getLogin(),
                            requestDto.getPassword()
                    )
            );

            log.debug("Authentication successful for: {}", requestDto.getLogin());

            String token = jwtService.generateToken(authentication.getName());

            AuthResponseDto response = AuthResponseDto.builder()
                    .token(token)
                    .expiresIn(86400L)  // 24 hours in seconds
                    .tokenType("Bearer")
                    .build();

            log.info("Login successful for: {}", requestDto.getLogin());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("Login failed for: {}", requestDto.getLogin(), e);
            throw e;
        }
    }
}
