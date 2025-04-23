package com.ugrasu.bordexback.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

public interface Mappable<Entity, Dto, SlimDto> {

    Entity toEntity(Dto dto);

    Entity toEntityFromSlim(SlimDto slimDto);

    Dto toDto(Entity entity);

    SlimDto toSlimDto(Entity entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Entity partialUpdate(Entity updatedBoardField, @MappingTarget Entity oldBoard);

}
