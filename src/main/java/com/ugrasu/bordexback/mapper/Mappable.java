package com.ugrasu.bordexback.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

public interface Mappable<Entity, Dto, SlimDto> {

    Entity toEntity(Dto dto);

    List<Entity> toEntities(List<Dto> dtos);

    Dto toDto(Entity entity);

    List<Dto> toDtos(List<Entity> entities);

    Entity toEntityFromSlimDto(SlimDto slimDto);

    List<Entity> toEntitiesFromSlimDtos(List<SlimDto> slimDtos);

    SlimDto toSlimDto(Entity entity);

    List<SlimDto> toSlimDtos(List<Entity> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Entity partialUpdate(Entity updatedBoardField, @MappingTarget Entity oldBoard);

}
