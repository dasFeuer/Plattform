package com.project.unknown.domain.dtos.postDto;

import com.project.unknown.domain.entities.userEntity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequestDto {
    private String title;
    private String content;
    private User author;

}
