package com.ugrasu.bordexback.controller;


import com.ugrasu.bordexback.dto.full.BoardDto;
import com.ugrasu.bordexback.entity.Board;
import com.ugrasu.bordexback.mapper.impl.BoardMapper;
import com.ugrasu.bordexback.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardMapper boardMapper;

    @GetMapping
    public PagedModel<BoardDto> findAll(Pageable pageable) {
        Page<Board> boards = boardService.findAll(pageable);
        Page<BoardDto> boardDtos = boards.map(boardMapper::toDto);
        return new PagedModel<>(boardDtos);
    }

    @GetMapping("/{id}")
    public BoardDto findById(@PathVariable Long id) {
        Board board = boardService.findOne(id);
        return boardMapper.toDto(board);
    }

    //TODO logged user
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoardDto save(@RequestBody BoardDto boardDto) {
        Board board = boardMapper.toEntity(boardDto);
        return boardMapper.toDto(boardService.save(board, null));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public BoardDto update(@PathVariable Long id, @RequestBody BoardDto boardDto) {
        Board board = boardMapper.toEntity(boardDto);
        Board patched = boardService.patch(id, board);
        return boardMapper.toDto(patched);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        boardService.delete(id);
    }
}