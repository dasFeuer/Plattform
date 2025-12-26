package com.project.unknown.mapper;

import com.project.unknown.domain.PatchUserDataRequest;
import com.project.unknown.domain.RegisterUserRequest;
import com.project.unknown.domain.UpdateUserDataRequest;
import com.project.unknown.domain.dtos.userDto.*;
import com.project.unknown.domain.entities.postEntity.Post;
import com.project.unknown.domain.entities.userEntity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto toDto(User user);
    UserProfileDto toUserProfile(User user);
    RegisterUserRequest toRegister(RegisterUserRequestDto registerUserRequestDto);
    UpdateUserDataRequest toUpdateUserDataRequest(UpdateUserDataRequestDto updateUserDataRequestDto);
    PatchUserDataRequest toPatchUserDataRequest(PatchUserDataRequestDto patchUserDataRequestDto);
}
