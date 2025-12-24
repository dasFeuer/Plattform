package com.project.unknown.domain.dtos.emailVerificationDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerificationRequestDto {
    @NotBlank(message = "Email is required")
    private String email;
}
