package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.mapper.impl.BoardMapper;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    public Page<Board> findAll(Specification<Board> specification, Pageable pageable) {
        return boardRepository.findAll(specification, pageable);
    }

    public Board findOne(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board with id %s not found".formatted(id)));
    }

    public Board save(Board board, User owner) {
        board.setId(null);
        board.setOwner(owner);
        return boardRepository.save(board);
    }

    public Board patch(Long oldBoardId, Board newBoard) {
        Board oldBoard = findOne(oldBoardId);
        Board updatedBoard = boardMapper.partialUpdate(newBoard, oldBoard);
        return boardRepository.save(updatedBoard);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }


}
