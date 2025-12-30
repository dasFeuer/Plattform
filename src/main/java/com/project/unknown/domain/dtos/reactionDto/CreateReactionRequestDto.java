package com.project.unknown.domain.dtos.reactionDto;

import com.project.unknown.enums.ReactionType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateReactionRequestDto {

    @NotNull(message = "Reaction type is required")
    private ReactionType type;

    private Long postId;
    private Long commentId;
}