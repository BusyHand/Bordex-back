package com.ugrasu.bordexback.mapper.impl;

import com.ugrasu.bordexback.dto.full.BoardDto;
import com.ugrasu.bordexback.dto.slim.BoardSlimDto;
import com.ugrasu.bordexback.entity.Board;
import com.ugrasu.bordexback.mapper.Mappable;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BoardMapper extends Mappable<Board, BoardDto, BoardSlimDto> {
}