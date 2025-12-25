package com.project.unknown.repository;

import com.project.unknown.domain.entities.postEntity.Post;
import com.project.unknown.domain.entities.reactionEntity.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
}
