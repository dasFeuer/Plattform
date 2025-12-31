package com.project.unknown.domain.entities.mediaEntity;

import com.project.unknown.domain.entities.postEntity.Post;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "media")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Media {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "file_path", nullable = false)
    private String filePath;  // "/uploads/posts/post_1_abc123.jpg"

    @Column(name = "file_type", nullable = false)
    private String fileType;  // "image/jpeg" oder "video/mp4"

    @Column(name = "file_size")
    private Long fileSize;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @CreatedDate
    @Column(name = "uploaded_at", nullable = false)
    private LocalDateTime uploadedAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Media media = (Media) o;
        return Objects.equals(id, media.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}