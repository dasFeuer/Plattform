package com.project.unknown.mapper;

import com.project.unknown.domain.dtos.mediaDto.MediaDto;
import com.project.unknown.domain.entities.mediaEntity.Media;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MediaMapper {
    MediaDto toDto(Media media);
    List<MediaDto> toDtoList(List<Media> mediaList);
}
