package com.ugrasu.bordexback.rest.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Set;

public interface WebMappable<Entity, Dto, SlimDto> {

    Entity toEntity(Dto dto);

    Entity toEntityFromSlim(SlimDto slimDto);

    Dto toDto(Entity entity);

    Set<Dto> toDto(Set<Entity> entity);

    SlimDto toSlimDto(Entity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Entity partialUpdate(Entity updatedBoardField, @MappingTarget Entity oldBoard);

}
