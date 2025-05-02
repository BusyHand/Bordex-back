package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.mapper.impl.BoardMapper;
import com.ugrasu.bordexback.rest.publisher.EventPublisher;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

import static com.ugrasu.bordexback.rest.event.EventType.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final EventPublisher eventPublisher;

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
        return eventPublisher.publish(BOARD_CREATED,
                boardRepository.save(board));
    }

    public Board patch(Long oldBoardId, Board newBoard) {
        Board oldBoard = findOne(oldBoardId);
        Board updatedBoard = boardMapper.partialUpdate(newBoard, oldBoard);
        return eventPublisher.publish(BOARD_UPDATED,
                boardRepository.save(updatedBoard));
    }

    @Transactional
    public Board delete(Long id) {
        Board board = findOne(id);
        Set<User> boardUsers = new HashSet<>(board.getBoardUsers());
        for (User user : boardUsers) {
            user.getUserBoards().remove(board);
        }
        board.getBoardUsers().clear();

        User owner = board.getOwner();
        if (owner != null) {
            owner.getBoards().remove(board);
            board.setOwner(null);
        }

        return eventPublisher.publish(BOARD_DELETED,
                boardRepository.deleteBoardById(board.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Board with id %s not found".formatted(board.getId()))));
    }
}
