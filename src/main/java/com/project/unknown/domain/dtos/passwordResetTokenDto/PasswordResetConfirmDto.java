package com.project.unknown.domain.dtos.passwordResetTokenDto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetConfirmDto {
    private String token;
    private String newPassword;
}
