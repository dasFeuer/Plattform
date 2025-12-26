package com.project.unknown.domain.dtos.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsDto {
    private Long id;

    private String firstName;

    private String lastName;

    private String username;

    private String email;

    private boolean verified;


}
