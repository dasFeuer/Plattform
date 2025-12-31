package com.project.unknown.mapper;

import com.project.unknown.domain.dtos.mediaDto.MediaDto;
import com.project.unknown.domain.dtos.postDto.*;
import com.project.unknown.domain.entities.mediaEntity.Media;
import com.project.unknown.domain.entities.postEntity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class}  //  UserMapper für Author mapping
)
public interface PostMapper {

    // Entity- DTOs
    // Post- PostSummaryDto
    @Mapping(target = "contentPreview", expression = "java(getContentPreview(post.getContent()))")
    @Mapping(target = "commentsCount", expression = "java((long) post.getComments().size())")
    @Mapping(target = "reactionsCount", expression = "java((long) post.getReactions().size())")
    @Mapping(target = "firstMedia", expression = "java(getFirstMedia(post))")
    PostSummaryDto toSummaryDto(Post post);

    // Post- PostDetailDto
    @Mapping(target = "commentsCount", expression = "java((long) post.getComments().size())")
    @Mapping(target = "reactionsCount", expression = "java((long) post.getReactions().size())")
    PostDetailDto toDetailDto(Post post);

    // Listen konvertieren
    List<PostSummaryDto> toSummaryDtoList(List<Post> posts);
    //List<PostDetailDto> toDetailDtoList(List<Post> posts);

    // Helper Methode für Content Preview
    default String getContentPreview(String content) {
        if (content == null) return null;
        return content.length() <= 200 ? content : content.substring(0, 200) + "...";
    }

    default MediaDto getFirstMedia(Post post) {
        if (post.getMediaFiles() == null || post.getMediaFiles().isEmpty()) {
            return null;
        }
        Media firstMedia = post.getMediaFiles().get(0);
        return MediaDto.builder()
                .id(firstMedia.getId())
                .fileName(firstMedia.getFileName())
                .filePath(firstMedia.getFilePath())
                .fileType(firstMedia.getFileType())
                .fileSize(firstMedia.getFileSize())
                .uploadedAt(firstMedia.getUploadedAt())
                .build();
    }
}
