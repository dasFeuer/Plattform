package com.project.unknown.repository;

import com.project.unknown.domain.entities.commentEntity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findByPostIdOrderByCreatedAtDesc(Long postId, Pageable pageable);
    Page<Comment> findByAuthorIdOrderByCreatedAtDesc(Long authorId, Pageable pageable);

    long countByAuthorId(Long authorId);
    long countByPostId(Long postId);
}
