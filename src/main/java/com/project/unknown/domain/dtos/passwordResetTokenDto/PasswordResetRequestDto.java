package com.project.unknown.domain.dtos.passwordResetTokenDto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequestDto {
    @NotBlank(message = "Email is required")
    private String email;
}
