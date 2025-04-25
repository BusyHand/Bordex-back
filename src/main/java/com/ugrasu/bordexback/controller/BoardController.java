package com.ugrasu.bordexback.controller;


import com.ugrasu.bordexback.dto.full.BoardDto;
import com.ugrasu.bordexback.dto.validation.OnCreate;
import com.ugrasu.bordexback.dto.validation.OnUpdate;
import com.ugrasu.bordexback.entity.Board;
import com.ugrasu.bordexback.mapper.impl.BoardMapper;
import com.ugrasu.bordexback.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Operation(summary = "Получить список досок", description = "Возвращает постраничный список досок")
    @GetMapping
    public PagedModel<BoardDto> findAll(Pageable pageable) {
        Page<Board> boards = boardService.findAll(pageable);
        Page<BoardDto> boardDtos = boards.map(boardMapper::toDto);
        return new PagedModel<>(boardDtos);
    }

    @Operation(summary = "Получить доску по ID", description = "Возвращает доску по её идентификатору")
    @GetMapping("/{id}")
    public BoardDto findById(@PathVariable("id") Long id) {
        Board board = boardService.findOne(id);
        return boardMapper.toDto(board);
    }

    @Operation(summary = "Создать новую доску", description = "Создаёт новую доску")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoardDto save(@Validated(OnCreate.class) @RequestBody BoardDto boardDto) {
        Board board = boardMapper.toEntity(boardDto);
        return boardMapper.toDto(boardService.save(board, null));
    }

    @Operation(summary = "Обновить доску", description = "Частично обновляет данные доски по ID")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BoardDto update(@PathVariable("id") Long id, @Validated(OnUpdate.class) @RequestBody BoardDto boardDto) {
        Board board = boardMapper.toEntity(boardDto);
        Board patched = boardService.patch(id, board);
        return boardMapper.toDto(patched);
    }

    @Operation(summary = "Удалить доску", description = "Удаляет доску по её идентификатору")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        boardService.delete(id);
    }
}
