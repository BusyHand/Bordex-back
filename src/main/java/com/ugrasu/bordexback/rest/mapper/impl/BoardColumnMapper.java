package com.ugrasu.bordexback.rest.mapper.impl;

import com.ugrasu.bordexback.rest.dto.web.full.BoardColumnDto;
import com.ugrasu.bordexback.rest.dto.web.slim.BoardColumnSlimDto;
import com.ugrasu.bordexback.rest.entity.BoardColumn;
import com.ugrasu.bordexback.rest.mapper.WebMappable;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface BoardColumnMapper extends WebMappable<BoardColumn, BoardColumnDto, BoardColumnSlimDto> {

}