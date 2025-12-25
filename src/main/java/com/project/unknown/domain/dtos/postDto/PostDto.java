package com.project.unknown.domain.dtos.postDto;

import com.project.unknown.domain.entities.commentEntity.Comment;
import com.project.unknown.domain.entities.reactionEntity.Reaction;
import com.project.unknown.domain.entities.userEntity.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;

    private String title;

    private String content;

    private User author;

    private List<Comment> comments = new ArrayList<>();

    private List<Reaction> reactions = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
