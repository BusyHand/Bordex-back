package com.ugrasu.bordexback.rest.mapper.impl;

import com.ugrasu.bordexback.rest.dto.full.BoardDto;
import com.ugrasu.bordexback.rest.dto.slim.BoardSlimDto;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.mapper.Mappable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface BoardMapper extends Mappable<Board, BoardDto, BoardSlimDto> {

    @Mapping(target = "tasksCount", expression = "java(countTasks(board))")
    @Mapping(target = "membersCount", expression = "java(countMembers(board))")
    BoardDto toDto(Board board);

    default Long countTasks(Board board) {
        return (long) board.getTasks().size();
    }

    default Long countMembers(Board board) {
        return (long) board.getBoardUsers().size();
    }
}