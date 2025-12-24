package com.project.unknown.controller;

import com.project.unknown.auth.JwtService;
import com.project.unknown.domain.RegisterUserRequest;
import com.project.unknown.domain.dtos.userDto.AuthResponseDto;
import com.project.unknown.domain.dtos.userDto.LoginUserRequestDto;
import com.project.unknown.domain.dtos.userDto.RegisterUserRequestDto;
import com.project.unknown.domain.dtos.userDto.UserDto;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.mapper.UserMapper;
import com.project.unknown.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final JwtService jwtService;
    private final AuthenticationManager authentication;

    @PostMapping("/register")
    public ResponseEntity<UserDto> registerUser(@RequestBody RegisterUserRequestDto registerUserRequestDto){
        RegisterUserRequest register = userMapper.toRegister(registerUserRequestDto);
        User user = userService.registerUser(register);
        UserDto dto = userMapper.toDto(user);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDto> loginUser(@RequestBody LoginUserRequestDto loginUserRequestDto){

        Authentication authenticate = authentication.authenticate(new UsernamePasswordAuthenticationToken
                (loginUserRequestDto.getLogin(),
                        loginUserRequestDto.getPassword()));
        String token = jwtService.generateToken(authenticate.getName());
        AuthResponseDto build = AuthResponseDto.builder()
                .token(token)
                .expiresIn(86400L)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(build);
    }
}
