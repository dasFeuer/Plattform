package com.project.unknown.mapper;

import com.project.unknown.domain.CreatePostRequest;
import com.project.unknown.domain.dtos.postDto.CreatePostRequestDto;
import com.project.unknown.domain.dtos.postDto.PostDto;
import com.project.unknown.domain.entities.postEntity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {
    PostDto toDto(Post post);
    CreatePostRequest toCreatePost(CreatePostRequestDto createPostRequestDto);
}
