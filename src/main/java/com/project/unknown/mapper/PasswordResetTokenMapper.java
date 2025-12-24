package com.project.unknown.mapper;

import com.project.unknown.domain.PasswordResetRequest;
import com.project.unknown.domain.dtos.passwordResetTokenDto.PasswordResetRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PasswordResetTokenMapper {
    //PasswordResetTokenDto toDto(PasswordResetToken passwordResetToken);
    PasswordResetRequest toResetPassword(PasswordResetRequestDto passwordResetRequestDto);
}
