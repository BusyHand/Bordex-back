package com.ugrasu.bordexback.rest.mapper.impl;

import com.ugrasu.bordexback.rest.dto.web.full.BoardDto;
import com.ugrasu.bordexback.rest.dto.web.slim.BoardSlimDto;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.mapper.WebMappable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface BoardMapper extends WebMappable<Board, BoardDto, BoardSlimDto> {

    @Mapping(
            target = "tasksCount",
            expression = "java(countTasks(board))"
    )
    @Mapping(
            target = "membersCount",
            expression = "java(countMembers(board))"
    )
    BoardDto toDto(Board board);

    default Long countTasks(Board board) {
        return board.getTasks() != null ? (long) board.getTasks().size() : 0L;
    }

    default Long countMembers(Board board) {
        return board.getBoardMembers() != null ? (long) board.getBoardMembers().size() : 0L;
    }
}