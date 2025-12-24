package com.project.unknown.domain.dtos.userDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorDto {

    private String firstName;

    private String lastName;

    private String username;
}
