package com.project.unknown.domain.dtos.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private LocalDateTime createdAt;

    private boolean verified;

    private Long totalPosts;
    private Long totalComments;
    private Long totalReactions;

}
