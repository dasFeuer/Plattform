package com.project.unknown.domain.dtos.userDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private boolean verified;

    private String profileImagePath;

    private LocalDateTime createdAt;
}
