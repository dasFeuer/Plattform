package com.project.unknown.domain.dtos.userDto;

import com.project.unknown.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserManagementDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private boolean verified;
    private Role role;
    private boolean banned;
    private LocalDateTime createdAt;
    private Long totalPosts;
    private Long totalComments;
    private Long totalReactions;
}