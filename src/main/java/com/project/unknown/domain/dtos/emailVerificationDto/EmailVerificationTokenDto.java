package com.project.unknown.domain.dtos.emailVerificationDto;

import com.project.unknown.domain.entities.userEntity.User;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationTokenDto {
    private Long id;

    private String token;

    private User user;

    private LocalDateTime expiryDate;

    private boolean used = false;
}