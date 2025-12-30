package com.project.unknown.mapper;

import com.project.unknown.domain.dtos.reactionDto.ReactionDto;
import com.project.unknown.domain.entities.reactionEntity.Reaction;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {UserMapper.class}
)
public interface ReactionMapper {

    ReactionDto toDto(Reaction reaction);

    List<ReactionDto> toDtoList(List<Reaction> reactions);
}
