package com.ugrasu.bordexback.rest.controller;


import com.ugrasu.bordexback.rest.controller.filter.BoardFilter;
import com.ugrasu.bordexback.rest.dto.full.BoardDto;
import com.ugrasu.bordexback.rest.dto.validation.OnCreate;
import com.ugrasu.bordexback.rest.dto.validation.OnUpdate;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.mapper.impl.BoardMapper;
import com.ugrasu.bordexback.rest.service.BoardService;
import com.ugrasu.bordexback.rest.service.UserService;
import com.ugrasu.bordexback.websocket.BoardWebSocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@Tag(name = "Доски", description = "Управление досками")
public class BoardController {

    private final BoardService boardService;
    private final BoardMapper boardMapper;

    // todo dev purpose
    private final UserService userService;
    private final BoardWebSocketService boardWebSocketService;

    @GetMapping
    @Operation(summary = "Получить список досок", description = "Возвращает постраничный список досок с фильтрацией")
    public PagedModel<BoardDto> findAll(@ParameterObject @ModelAttribute BoardFilter filter,
                                        @ParameterObject Pageable pageable) {
        Specification<Board> specification = filter.toSpecification();
        Page<Board> boards = boardService.findAll(specification, pageable);
        Page<BoardDto> boardDtos = boards.map(boardMapper::toDto);
        return new PagedModel<>(boardDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить доску по ID", description = "Возвращает доску по её идентификатору")
    public BoardDto findById(@PathVariable("id") Long id) {
        Board board = boardService.findOne(id);
        return boardMapper.toDto(board);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать новую доску", description = "Создаёт новую доску")
    public BoardDto save(@Validated(OnCreate.class) @RequestBody BoardDto boardDto) {
        Board board = boardMapper.toEntity(boardDto);
        Board saved = boardService.save(board, userService.findOne(1L));

        BoardDto dto = boardMapper.toDto(saved);
        boardWebSocketService.sendUserBoardsUpdate(dto);
        return dto;
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновить доску", description = "Частично обновляет данные доски по ID")
    public BoardDto update(@PathVariable("id") Long id, @Validated(OnUpdate.class) @RequestBody BoardDto boardDto) {
        Board board = boardMapper.toEntity(boardDto);
        Board patched = boardService.patch(id, board);

        BoardDto dto = boardMapper.toDto(patched);
        boardWebSocketService.sendUserBoardsUpdate(dto);
        return dto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить доску", description = "Удаляет доску по её идентификатору")
    public void delete(@PathVariable("id") Long id) {
        Board boardToDelete = boardService.findOne(id);
        boardService.delete(id);

        boardWebSocketService.sendUserBoardsDelete(boardMapper.toDto(boardToDelete));
    }
}
