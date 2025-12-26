package com.project.unknown.domain.dtos.postDto;

import com.project.unknown.domain.dtos.commentDto.CommentDto;
import com.project.unknown.domain.dtos.reactionDto.ReactionDto;
import com.project.unknown.domain.dtos.userDto.AuthorDto;
import com.project.unknown.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDetailDto {
    private Long id;

    private String title;

    private String content;

    private AuthorDto author;

    private List<CommentDto> recentComments;
    private Long totalCommentsCount;

    private Map<ReactionType, Long> reactionSummary;
    private ReactionDto currentUserReaction;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
