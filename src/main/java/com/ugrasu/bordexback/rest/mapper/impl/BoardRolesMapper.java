package com.ugrasu.bordexback.rest.mapper.impl;

import com.ugrasu.bordexback.rest.dto.web.full.BoardRolesDto;
import com.ugrasu.bordexback.rest.dto.web.slim.BoardRolesSlimDto;
import com.ugrasu.bordexback.rest.entity.BoardRoles;
import com.ugrasu.bordexback.rest.mapper.WebMappable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface BoardRolesMapper extends WebMappable<BoardRoles, BoardRolesDto, BoardRolesSlimDto> {

    @Mapping(
            target = "userId",
            expression = "java(boardRoles.getUser().getId())"

    )
    @Mapping(
            target = "boardId",
            expression = "java(boardRoles.getBoard().getId())"

    )
    @Override
    BoardRolesSlimDto toSlimDto(BoardRoles boardRoles);

}