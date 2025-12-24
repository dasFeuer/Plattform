package com.project.unknown.mapper;

import com.project.unknown.domain.EmailVerificationRequest;
import com.project.unknown.domain.dtos.emailVerificationDto.EmailVerificationRequestDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EmailVerificationTokenMapper {
    // EmailVerificationTokenDto toDto(EmailVerificationToken emailVerificationToken);
    EmailVerificationRequest toVerificationEmail(EmailVerificationRequestDto emailVerificationRequestDto);
}
