package com.project.unknown.domain.dtos.commentDto;

import com.project.unknown.domain.dtos.userDto.AuthorDto;
import com.project.unknown.domain.entities.postEntity.Post;
import com.project.unknown.domain.entities.reactionEntity.Reaction;
import com.project.unknown.domain.entities.userEntity.User;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    private String content;

    private Post post;

    private AuthorDto author;

    private List<Reaction> reactions = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}