package com.project.unknown.domain.dtos.reactionDto;

import com.project.unknown.enums.ReactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReactionsSummaryDto {

    private Map<ReactionType, Long> counts;
    private Long totalReactions;
    private ReactionDto currentUserReaction;
}