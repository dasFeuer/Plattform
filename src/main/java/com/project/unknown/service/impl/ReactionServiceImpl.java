package com.project.unknown.service.impl;

import com.project.unknown.domain.dtos.reactionDto.CreateReactionRequestDto;
import com.project.unknown.domain.dtos.reactionDto.ReactionDto;
import com.project.unknown.domain.dtos.reactionDto.ReactionsSummaryDto;
import com.project.unknown.domain.entities.commentEntity.Comment;
import com.project.unknown.domain.entities.postEntity.Post;
import com.project.unknown.domain.entities.reactionEntity.Reaction;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.enums.ReactionType;
import com.project.unknown.exception.ResourceNotFoundException;
import com.project.unknown.mapper.ReactionMapper;
import com.project.unknown.repository.ReactionRepository;
import com.project.unknown.service.CommentService;
import com.project.unknown.service.PostService;
import com.project.unknown.service.ReactionService;
import com.project.unknown.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReactionServiceImpl implements ReactionService {

    private final ReactionRepository reactionRepository;
    private final PostService postService;
    private final CommentService commentService;
    private final UserService userService;
    private final ReactionMapper reactionMapper;


    @Override
    @Transactional
    public ReactionDto addOrUpdateReaction(CreateReactionRequestDto requestDto, String userEmail) {
        log.info("Adding/updating reaction for user: {}", userEmail);

        if(requestDto.getPostId() == null && requestDto.getCommentId() == null){
            throw new IllegalArgumentException("Either postId or commentId must be provided");
        }

        if (requestDto.getPostId() != null && requestDto.getCommentId() != null) {
            throw new IllegalArgumentException("Cannot react to both post and comment at the same time");
        }

        User user = userService.getUserEntityByEmail(userEmail);

        Reaction reaction;


        if (requestDto.getPostId() != null){
            // Post Reaction
            Post post = postService.getPostEntityById(requestDto.getPostId());

            Optional<Reaction> existingReaction  = reactionRepository.findByUserIdAndPostId(user.getId(), post.getId());

            if(existingReaction.isPresent()){
                reaction = existingReaction.get();
                reaction.setType(requestDto.getType());
                log.info("Updating existing post reaction ID: {}", reaction.getId());
            } else {
                reaction = Reaction.builder()
                        .type(requestDto.getType())
                        .user(user)
                        .post(post)
                        .createdAt(LocalDateTime.now())
                        .build();
                log.info("Creating new post reaction");
            }
        } else {
            // Comment Reaction
            Comment comment = commentService.getCommentEntityById(requestDto.getCommentId());

            Optional<Reaction> existingReaction = reactionRepository.findByUserIdAndCommentId(user.getId(), comment.getId());

            if(existingReaction.isPresent()){
                reaction = existingReaction.get();
                reaction.setType(requestDto.getType());
                log.info("Updating existing comment reaction ID: {}", reaction.getId());
            } else {
                reaction = Reaction.builder()
                        .type(requestDto.getType())
                        .user(user)
                        .comment(comment)
                        .createdAt(LocalDateTime.now())
                        .build();
                log.info("Creating new comment reaction");
            }
        }

        Reaction savedReaction = reactionRepository.save(reaction);
        log.info("Reaction saved successfully with ID: {}", savedReaction.getId());

        return reactionMapper.toDto(savedReaction);

    }

    @Override
    @Transactional
    public void removeReaction(Long reactionId, String userEmail) {
        log.info("Removing reaction with ID: {}", reactionId);

        Reaction reaction = reactionRepository.findById(reactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Reaction with ID " + reactionId + " not found"));

        // Nur eigene Reactions löschen
        User user = userService.getUserEntityByEmail(userEmail);
        if (!reaction.getUser().getId().equals(user.getId())) {
            log.warn("User {} tried to remove reaction {} owned by {}",
                    user.getId(), reactionId, reaction.getUser().getId());
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "You can only remove your own reactions"
            );
        }

        reactionRepository.deleteById(reactionId);
        log.info("Reaction {} removed successfully", reactionId);
    }

    @Override
    public ReactionsSummaryDto getPostReactionsSummary(Long postId, String currentUserEmail) {
        log.debug("Fetching reactions summary for post ID: {}", postId);

        postService.getPostEntityById(postId);

        // Reactions count nach Type gruppieren
        List<Object[]> results = reactionRepository.countByPostIdGroupByType(postId);

        Map<ReactionType, Long> counts = new HashMap<>();
        long totalReactions = 0;

        for (Object[] result : results) {
            ReactionType type = (ReactionType) result[0];
            Long count = (Long) result[1];
            counts.put(type, count);
            totalReactions += count;
        }

        // Current user's reaction
        ReactionDto currentUserReaction = null;
        if (currentUserEmail != null) {
            Optional<User> currentUser = userService.findUserByEmail(currentUserEmail);
            if (currentUser.isPresent()) {
                Optional<Reaction> userReaction = reactionRepository.findByUserIdAndPostId(
                        currentUser.get().getId(),
                        postId
                );
                currentUserReaction = userReaction.map(reactionMapper::toDto).orElse(null);
            }
        }

        return ReactionsSummaryDto.builder()
                .counts(counts)
                .totalReactions(totalReactions)
                .currentUserReaction(currentUserReaction)
                .build();
    }

    @Override
    public ReactionsSummaryDto getCommentReactionsSummary(Long commentId, String currentUserEmail) {
        log.debug("Fetching reactions summary for comment ID: {}", commentId);

        commentService.getCommentEntityById(commentId);

        List<Object[]> results = reactionRepository.countByCommentIdGroupByType(commentId);

        Map<ReactionType, Long> counts = new HashMap<>();
        long totalReactions = 0;

        for (Object[] result : results) {
            ReactionType type = (ReactionType) result[0];
            Long count = (Long) result[1];
            counts.put(type, count);
            totalReactions += count;
        }

        ReactionDto currentUserReaction = null;
        if (currentUserEmail != null) {
            Optional<User> currentUser = userService.findUserByEmail(currentUserEmail);
            if (currentUser.isPresent()) {
                Optional<Reaction> userReaction = reactionRepository.findByUserIdAndCommentId(
                        currentUser.get().getId(),
                        commentId
                );
                currentUserReaction = userReaction.map(reactionMapper::toDto).orElse(null);
            }
        }

        return ReactionsSummaryDto.builder()
                .counts(counts)
                .totalReactions(totalReactions)
                .currentUserReaction(currentUserReaction)
                .build();
    }
}
