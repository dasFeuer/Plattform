package com.project.unknown.mapper;

import com.project.unknown.domain.PatchUserDataRequest;
import com.project.unknown.domain.RegisterUserRequest;
import com.project.unknown.domain.UpdateUserDataRequest;
import com.project.unknown.domain.dtos.userDto.PatchUserDataRequestDto;
import com.project.unknown.domain.dtos.userDto.RegisterUserRequestDto;
import com.project.unknown.domain.dtos.userDto.UpdateUserDataRequestDto;
import com.project.unknown.domain.dtos.userDto.UserDto;
import com.project.unknown.domain.entities.userEntity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserDto toDto(User user);
    RegisterUserRequest toRegister(RegisterUserRequestDto registerUserRequestDto);
    UpdateUserDataRequest toUpdateUserDataRequest(UpdateUserDataRequestDto updateUserDataRequestDto);
    PatchUserDataRequest toPatchUserDataRequest(PatchUserDataRequestDto patchUserDataRequestDto);
}
