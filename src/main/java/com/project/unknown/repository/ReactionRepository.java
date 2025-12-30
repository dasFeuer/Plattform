package com.project.unknown.repository;

import com.project.unknown.domain.entities.reactionEntity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    // Post Reactions

    // Reaction eines Users auf einen Post finden
    Optional<Reaction> findByUserIdAndPostId(Long userId, Long postId);

    // Alle Reactions eines Posts
    List<Reaction> findByPostId(Long postId);

    // Anzahl Reactions eines Posts
    long countByPostId(Long postId);

    @Query("SELECT r.type, COUNT(r) FROM Reaction r WHERE r.post.id = :postId GROUP BY r.type")
    List<Object[]> countByPostIdGroupByType(@Param("postId") Long postId);


    // Comment Reactions

    // Reaction eines Users auf einen Comment finden
    Optional<Reaction> findByUserIdAndCommentId(Long userId, Long commentId);

    // Alle Reactions eines Comments
    List<Reaction> findByCommentId(Long commentId);

    // Anzahl Reactions eines Comments
    long countByCommentId(Long commentId);


    // Anzahl Reactions pro Type für einen Comment
    @Query("SELECT r.type, COUNT(r) FROM Reaction r WHERE r.comment.id = :commentId GROUP BY r.type")
    List<Object[]> countByCommentIdGroupByType(@Param("commentId") Long commentId);


    // User Reactions

    // Alle Reactions eines Users
    List<Reaction> findByUserId(Long userId);

    long countByUserId(Long userId);
}
