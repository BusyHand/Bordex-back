package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.rest.controller.filter.BoardFilter;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ugrasu.bordexback.rest.event.EventType.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;
    private final EventPublisher eventPublisher;

    @PreAuthorize(
            """
                    (@use.isTheSameUser(#filter.memberIds()))"""
    )
    public Page<Board> findAll(@P("filter") BoardFilter filter, Pageable pageable) {
        return boardRepository.findAll(filter.filter(), pageable);
    }

    @PreAuthorize("@bse.isMember(#id)")
    public Board findOne(@P("id") Long id) {
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
    @PreAuthorize("@bse.isOwner(#oldBoardId)")
    public Board patch(@P("oldBoardId") Long oldBoardId, Board newBoard) {
        Board oldBoard = findOne(oldBoardId);
        Board updatedBoard = boardMapper.partialUpdate(newBoard, oldBoard);
        return eventPublisher.publish(BOARD_UPDATED, updatedBoard);
    }

    @Transactional
    @PreAuthorize("@bse.isOwner(#id)")
    public void delete(@P("id") Long id) {
        Board board = findOne(id);
        eventPublisher.publish(BOARD_DELETED, board);
        board.deleteAll();
        boardRepository.deleteById(id);
    }

    public Board addUser(Long boardId, User user) {
        Board board = findOne(boardId);
        board.addMember(user);
        return eventPublisher.publish(BOARD_ASSIGNED, board);
    }

    public Board removeUser(Long boardId, User unassignUser) {
        Board board = findOne(boardId);
        board.removeMember(unassignUser);
        return eventPublisher.publish(BOARD_UNASSIGNED, board, unassignUser);
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
