package com.project.unknown.service;

import com.project.unknown.domain.dtos.reactionDto.CreateReactionRequestDto;
import com.project.unknown.domain.dtos.reactionDto.ReactionDto;
import com.project.unknown.domain.dtos.reactionDto.ReactionsSummaryDto;

public interface ReactionService {

    ReactionDto addOrUpdateReaction(CreateReactionRequestDto requestDto, String userEmail);

    void removeReaction(Long reactionId, String userEmail);

    ReactionsSummaryDto getPostReactionsSummary(Long postId, String currentUserEmail);

    ReactionsSummaryDto getCommentReactionsSummary(Long commentId, String currentUserEmail);
}
