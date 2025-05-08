package com.ugrasu.bordexback.rest.controller;


import com.ugrasu.bordexback.auth.security.AuthenficatedUser;
import com.ugrasu.bordexback.rest.controller.filter.BoardFilter;
import com.ugrasu.bordexback.rest.controller.validation.OnCreate;
import com.ugrasu.bordexback.rest.controller.validation.OnUpdate;
import com.ugrasu.bordexback.rest.dto.web.full.BoardDto;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.facade.BoardFacadeManagement;
import com.ugrasu.bordexback.rest.mapper.impl.BoardMapper;
import com.ugrasu.bordexback.rest.service.BoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Доски",
        description = "Управление досками"
)
@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class BoardController {

    private final BoardFacadeManagement boardFacadeManagement;
    private final BoardService boardService;
    private final BoardMapper boardMapper;

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
    public BoardDto create(@AuthenticationPrincipal AuthenficatedUser authenficatedUser,
                           @Validated(OnCreate.class) @RequestBody BoardDto boardDto) {
        Board board = boardMapper.toEntity(boardDto);
        Board saved = boardFacadeManagement.createBoard(board, authenficatedUser.getUserId());
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

    @Operation(
            summary = "Добавить пользователя к доске",
            description = "Добавить пользователя по идентификатору доски и пользователя"
    )
    @PatchMapping("/{board-id}/add-user/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    public BoardDto addUser(@PathVariable("board-id") Long boardId,
                            @PathVariable("user-id") Long userId) {
        Board board = boardFacadeManagement.addUserToBoard(boardId, userId);
        return boardMapper.toDto(board);
    }

    @Operation(
            summary = "Удалить пользователя с доски",
            description = "Удалить пользователя с доски по идентификатору доски и пользователя"
    )
    @PatchMapping("/{board-id}/remove-user/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    public BoardDto removeUser(@PathVariable("board-id") Long boardId,
                               @PathVariable("user-id") Long userId) {
        Board board = boardFacadeManagement.removeUserFromBoard(boardId, userId);
        return boardMapper.toDto(board);
    }

    @Operation(
            summary = "Передать владельца доски другому пользователю",
            description = "Передать владельца доски другому пользователю по его идентификатору"
    )
    @PatchMapping("/{board-id}/owner-transfer/{new-owner-id}")
    @ResponseStatus(HttpStatus.OK)
    public BoardDto ownerTransfer(@PathVariable("board-id") Long boardId,
                                  @PathVariable("new-owner-id") Long newOwnerId) {
        Board board = boardFacadeManagement.ownerTransfer(boardId, newOwnerId);
        return boardMapper.toDto(board);
    }
}
