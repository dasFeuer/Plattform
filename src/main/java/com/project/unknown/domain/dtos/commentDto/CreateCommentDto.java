package com.project.unknown.domain.dtos.commentDto;

import com.project.unknown.domain.dtos.reactionDto.ReactionDto;
import com.project.unknown.domain.dtos.userDto.AuthorDto;
import com.project.unknown.domain.entities.postEntity.Post;
import com.project.unknown.domain.entities.reactionEntity.Reaction;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.enums.ReactionType;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    private Long id;

    private String content;

    private AuthorDto author;

    private Long reactionsCount;
    private Map<ReactionType, Long> reactionsSummary;
    private ReactionDto currentUserReaction;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}