package com.project.unknown.domain.dtos.userDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserRequestDto {

    @NotBlank(message = "Login (username or email) is required")
    private String login;

    @NotBlank(message = "Password is required")
    private String password;

}
