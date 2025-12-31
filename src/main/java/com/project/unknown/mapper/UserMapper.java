package com.project.unknown.mapper;


import com.project.unknown.domain.dtos.userDto.*;
import com.project.unknown.domain.entities.userEntity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    @Mapping(target = "profileImagePath", source = "profileImagePath")
    UserResponseDto toResponseDto(User user);
    UserProfileDto toProfileDto(User user);

    @Mapping(target = "profileImagePath", source = "profileImagePath")
    AuthorDto toAuthorDto(User user);

    List<UserResponseDto> toResponseDtoList(List<User> users);
    // List<UserProfileDto> toProfileDtoList(List<User> users);
}
