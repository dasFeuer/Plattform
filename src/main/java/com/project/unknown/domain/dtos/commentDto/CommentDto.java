package com.project.unknown.domain.dtos.commentDto;

import com.project.unknown.domain.dtos.userDto.AuthorDto;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    private String content;

    private AuthorDto author;

    private Long reactionsCount;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}