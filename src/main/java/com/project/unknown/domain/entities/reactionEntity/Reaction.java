package com.project.unknown.domain.entities.reactionEntity;

import com.project.unknown.domain.entities.commentEntity.Comment;
import com.project.unknown.domain.entities.postEntity.Post;
import com.project.unknown.domain.entities.userEntity.User;
import com.project.unknown.enums.ReactionType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(
        name = "reactions",
        uniqueConstraints = {
                // Ein User, nur eine Reaktion pro Post
                @UniqueConstraint(columnNames = {"user_id", "post_id"}),
                // Ein User, nur eine Reaktion pro Comment
                @UniqueConstraint(columnNames = {"user_id", "comment_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ReactionType type;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reaction reaction = (Reaction) o;
        return Objects.equals(id, reaction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}