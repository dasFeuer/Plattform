package com.project.unknown.domain.dtos.reactionDto;

import com.project.unknown.domain.entities.commentEntity.Comment;
import com.project.unknown.domain.entities.postEntity.Post;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.enums.ReactionType;
import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionDto {

    private Long id;

    private ReactionType type;

    private User user;

    private Post post;

    private Comment comment;

    private LocalDateTime createdAt;
}