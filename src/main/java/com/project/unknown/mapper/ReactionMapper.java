package com.project.unknown.mapper;

import com.project.unknown.domain.dtos.commentDto.CommentDto;
import com.project.unknown.domain.entities.commentEntity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class}  //  UserMapper für Author mapping
)
public interface CommentMapper {

    @Mapping(target = "reactionsCount", expression = "java((long) comment.getReactions().size())")
    CommentDto toDto(Comment comment);

    List<CommentDto> toDtoList(List<Comment> comments);
}
