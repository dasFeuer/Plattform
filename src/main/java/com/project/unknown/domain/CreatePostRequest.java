package com.project.unknown.domain;

import com.project.unknown.domain.entities.commentEntity.Comment;
import com.project.unknown.domain.entities.reactionEntity.Reaction;
import com.project.unknown.domain.entities.userEntity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreatePostRequest {
    private Long id;

    private String title;

    private String content;

    private User author;

    private List<Comment> comments = new ArrayList<>();

    private List<Reaction> reactions = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}
