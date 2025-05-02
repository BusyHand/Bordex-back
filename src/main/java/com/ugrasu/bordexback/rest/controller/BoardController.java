package com.ugrasu.bordexback.rest.controller;


import com.ugrasu.bordexback.rest.controller.filter.BoardFilter;
import com.ugrasu.bordexback.rest.controller.validation.OnCreate;
import com.ugrasu.bordexback.rest.controller.validation.OnUpdate;
import com.ugrasu.bordexback.rest.dto.web.full.BoardDto;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.mapper.impl.BoardMapper;
import com.ugrasu.bordexback.rest.service.BoardService;
import com.ugrasu.bordexback.rest.service.UserService;
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

@Tag(
        name = "Доски",
        description = "Управление досками"
)
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardMapper boardMapper;

    // todo not auth enable
    private final UserService userService;

    //todo add user
    //todo delete user

    @Operation(
            summary = "Получить список досок",
            description = "Возвращает постраничный список досок с фильтрацией"
    )
    @GetMapping
    public PagedModel<BoardDto> findAll(@ParameterObject @ModelAttribute BoardFilter filter,
                                        @ParameterObject Pageable pageable) {
        Specification<Board> specification = filter.toSpecification();
        Page<Board> boards = boardService.findAll(specification, pageable);
        Page<BoardDto> boardDtos = boards.map(boardMapper::toDto);
        return new PagedModel<>(boardDtos);
    }

    @Operation(
            summary = "Получить доску по ID",
            description = "Возвращает доску по её идентификатору"
    )
    @GetMapping("/{id}")
    public BoardDto findById(@PathVariable("id") Long id) {
        Board board = boardService.findOne(id);
        return boardMapper.toDto(board);
    }

    @Operation(
            summary = "Создать новую доску",
            description = "Создаёт новую доску"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoardDto create(@Validated(OnCreate.class) @RequestBody BoardDto boardDto) {
        Board board = boardMapper.toEntity(boardDto);
        Board saved = boardService.save(board, userService.findOne(1L));
        return boardMapper.toDto(saved);
    }

    @Operation(
            summary = "Обновить доску",
            description = "Частично обновляет данные доски по ID"
    )
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BoardDto update(@PathVariable("id") Long id, @Validated(OnUpdate.class) @RequestBody BoardDto boardDto) {
        Board board = boardMapper.toEntity(boardDto);
        Board patched = boardService.patch(id, board);
        return boardMapper.toDto(patched);
    }

    @Operation(
            summary = "Удалить доску",
            description = "Удаляет доску по её идентификатору"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        boardService.delete(id);
    }

}
