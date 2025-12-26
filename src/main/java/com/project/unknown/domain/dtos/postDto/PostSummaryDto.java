package com.project.unknown.domain.dtos.postDto;

import com.project.unknown.domain.dtos.userDto.AuthorDto;
import com.project.unknown.domain.entities.commentEntity.Comment;
import com.project.unknown.domain.entities.reactionEntity.Reaction;
import com.project.unknown.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostSummaryDto {
    private Long id;

    private String title;

    private String contentPreview;

    private AuthorDto author;

    private Long commentsCount;
    private Long reactionsCount;

    private Map<ReactionType, Long> reactionSummary;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
