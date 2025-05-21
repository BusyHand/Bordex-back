package com.ugrasu.bordexback.rest.controller;


import com.ugrasu.bordexback.rest.dto.web.full.BoardColumnDto;
import com.ugrasu.bordexback.rest.dto.web.full.BoardDto;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.BoardColumn;
import com.ugrasu.bordexback.rest.facade.BoardFacadeManagement;
import com.ugrasu.bordexback.rest.mapper.impl.BoardColumnMapper;
import com.ugrasu.bordexback.rest.mapper.impl.BoardMapper;
import com.ugrasu.bordexback.rest.publisher.EventPublisher;
import com.ugrasu.bordexback.rest.service.BoardColumnService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static com.ugrasu.bordexback.rest.event.EventType.BOARD_COLUMN_REMOVE;

@Tag(
        name = "Доски",
        description = "Управление досками"
)
@RestController
@RequestMapping("/api/columns")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class BoardColumnController {

    private final BoardColumnMapper boardColumnMapper;
    private final BoardFacadeManagement boardFacadeManagement;
    private final BoardColumnService boardColumnService;
    private final BoardMapper boardMapper;
    private final EventPublisher eventPublisher;

    @Operation(
            summary = "Добавить колонку",
            description = "Добавить колонку к доске"
    )
    @PostMapping("/{board-id}")
    @ResponseStatus(HttpStatus.OK)
    public BoardDto addColumn(@RequestBody BoardColumnDto dto,
                              @PathVariable("board-id") Long boardId) {
        BoardColumn boardColumn = boardColumnMapper.toEntity(dto);
        Board board = boardFacadeManagement.addColumn(boardId, boardColumn);
        return boardMapper.toDto(board);
    }

    @Operation(
            summary = "Обновить колонку",
            description = "Обновить колонку"
    )
    @PatchMapping("/{column-id}")
    @ResponseStatus(HttpStatus.OK)
    public BoardColumnDto update(@RequestBody BoardColumnDto dto,
                                 @PathVariable("column-id") Long columnId) {
        BoardColumn boardColumn = boardColumnMapper.toEntity(dto);
        boardColumn = boardColumnService.patch(columnId, boardColumn);
        return boardColumnMapper.toDto(boardColumn);
    }

    @Operation(
            summary = "Добавить колонку",
            description = "Добавить колонку к доске"
    )
    @DeleteMapping("/{column-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeColumn(@PathVariable("column-id") Long columnId) {
        BoardColumn boardColumn = boardColumnService.find(columnId);
        Board board = boardColumn.getBoard();
        boardFacadeManagement.deleteColumn(columnId);
        board.getBoardColumns().remove(boardColumn);
        eventPublisher.publish(BOARD_COLUMN_REMOVE, board);
    }

}
