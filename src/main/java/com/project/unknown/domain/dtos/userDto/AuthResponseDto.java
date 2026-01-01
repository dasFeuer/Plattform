package com.project.unknown.domain.dtos.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponseDto {
    private String token;
    private String tokenType;
    private long expiresIn;

    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
