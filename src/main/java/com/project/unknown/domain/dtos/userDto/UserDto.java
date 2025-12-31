package com.project.unknown.domain.dtos.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

//    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String profileImagePath;

    private boolean verified = false;

    private LocalDateTime verifiedAt;
}
