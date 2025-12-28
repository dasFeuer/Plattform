package com.project.unknown.mapper;

import com.project.unknown.domain.dtos.postDto.*;
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
    PostSummaryDto toSummaryDto(Post post);

    // Post- PostDetailDto
    @Mapping(target = "commentsCount", expression = "java((long) post.getComments().size())")
    @Mapping(target = "reactionsCount", expression = "java((long) post.getReactions().size())")
    PostDetailDto toDetailDto(Post post);

    // Listen konvertieren
    List<PostSummaryDto> toSummaryDtoList(List<Post> posts);
    List<PostDetailDto> toDetailDtoList(List<Post> posts);

    // Helper Methode für Content Preview
    default String getContentPreview(String content) {
        if (content == null) {
            return null;
        }
        if (content.length() <= 200) {
            return content;
        }
        return content.substring(0, 200) + "...";
    }
}
