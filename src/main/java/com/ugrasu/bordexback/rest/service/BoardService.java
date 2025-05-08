package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.exception.UserNotBoardMemberException;
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

    @Transactional
    public Board save(Board board, User owner) {
        if (board.getId() != null) {
            board.setId(null);
        }
        board.setOwner(owner);
        board.addMember(owner);
        Board saved = boardRepository.save(board);
        return eventPublisher.publish(BOARD_CREATED, saved);
    }

    @Transactional
    public Board patch(Long oldBoardId, Board newBoard) {
        Board oldBoard = findOne(oldBoardId);
        Board updatedBoard = boardMapper.partialUpdate(newBoard, oldBoard);
        return eventPublisher.publish(BOARD_UPDATED, updatedBoard);
    }

    @Transactional
    public void delete(Long id) {
        Board board = findOne(id);
        board.getTasks().clear();
        boardRepository.deleteBoardById(id);
    }

    public Board addUser(Long boardId, User user) {
        Board board = findOne(boardId);
        board.addMember(user);
        return eventPublisher.publish(BOARD_ASSIGNED, board);
    }

    public Board removeUser(Long boardId, User user) {
        Board board = findOne(boardId);
        board.removeMember(user);
        return eventPublisher.publish(BOARD_UNASSIGNED, board);
    }

    public Board ownerTransfer(Long boardId, User newOwner) {
        Board board = findOne(boardId);
        if (!board.isMember(newOwner)) {
            throw new UserNotBoardMemberException("User with %s username is not board member of board with %s id".formatted(newOwner.getUsername(), boardId));
        }
        board.setOwner(newOwner);
        return eventPublisher.publish(BOARD_OWNER_CHANGED, board);
    }

}
