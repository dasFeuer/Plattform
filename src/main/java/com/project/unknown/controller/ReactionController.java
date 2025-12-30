package com.project.unknown.controller;

import com.project.unknown.domain.dtos.reactionDto.CreateReactionRequestDto;
import com.project.unknown.domain.dtos.reactionDto.ReactionDto;
import com.project.unknown.domain.dtos.reactionDto.ReactionsSummaryDto;
import com.project.unknown.service.ReactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reactions")
@Slf4j
public class ReactionController {
    private final ReactionService reactionService;

    @PostMapping
    public ResponseEntity<ReactionDto> addOrUpdateReaction(
            @Valid @RequestBody CreateReactionRequestDto requestDto,
            Authentication authentication) {

        log.info("POST request to add/update reaction");
        String authorEmail = authentication.getName();
        ReactionDto reaction = reactionService.addOrUpdateReaction(requestDto, authorEmail);
        log.info("Reaction added/updated successfully with ID: {}", reaction.getId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(reaction);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeReaction(
            @PathVariable Long id,
            Authentication authentication) {

        log.info("DELETE request for reaction with ID: {}", id);

        String userEmail = authentication.getName();
        reactionService.removeReaction(id, userEmail);

        log.info("Reaction {} removed successfully", id);

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/post/{postId}/summary")
    public ResponseEntity<ReactionsSummaryDto> getPostReactionsSummary(
            @PathVariable Long postId,
            Authentication authentication) {

        log.info("GET request for reactions summary of post ID: {}", postId);

        String currentUserEmail = authentication != null ? authentication.getName() : null;
        ReactionsSummaryDto summary = reactionService.getPostReactionsSummary(postId, currentUserEmail);

        return ResponseEntity.ok(summary);
    }

    @GetMapping("/comment/{commentId}/summary")
    public ResponseEntity<ReactionsSummaryDto> getCommentReactionsSummary(
            @PathVariable Long commentId,
            Authentication authentication) {

        log.info("GET request for reactions summary of comment ID: {}", commentId);

        String currentUserEmail = authentication != null ? authentication.getName() : null;
        ReactionsSummaryDto summary = reactionService.getCommentReactionsSummary(commentId, currentUserEmail);

        return ResponseEntity.ok(summary);
    }
}
