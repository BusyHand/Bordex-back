package com.ugrasu.bordexback.rest.facade;

import com.ugrasu.bordexback.rest.entity.BaseEntity;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.BoardColumn;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import com.ugrasu.bordexback.rest.publisher.EventPublisher;
import com.ugrasu.bordexback.rest.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

import static com.ugrasu.bordexback.rest.event.EventType.BOARD_COLUMN_REMOVE;

@Service
@RequiredArgsConstructor
public class BoardFacadeManagement {

    private final UserService userService;
    private final BoardRolesService boardRolesService;
    private final BoardService boardService;
    private final BoardColumnService boardColumnService;
    private final TaskService taskService;

    @Transactional
    @PreAuthorize("@bse.isOwner(#boardId)")
    public Board ownerTransfer(@P("boardId") Long boardId, Long newOwnerId) {
        User newOwner = userService.findOne(newOwnerId);
        Board board = boardService.ownerTransfer(boardId, newOwner);
        boardRolesService.patch(newOwner.getId(), board.getId(), Set.of(BoardRole.values()));
        return board;
    }

    @Transactional
    public Board createBoard(Board board, Long userId) {
        User owner = userService.findOne(userId);
        Board createdBoard = boardService.save(board, owner);
        boardRolesService.save(owner, createdBoard, Set.of(BoardRole.values()));
        return createdBoard;
    }

    @Transactional
    @PreAuthorize(
            """
                    (@bse.isOwner(#boardId)
                    or (!@bse.isPrivate(#boardId) and @brse.hasBoardRole(#boardId, 'MANAGER')))"""
    )
    public Board addUserToBoard(@P("boardId") Long boardId, @P("userId") Long userId) {
        User user = userService.findOne(userId);
        Board board = boardService.addUser(boardId, user);
        boardRolesService.save(user, board, Set.of(BoardRole.VIEWER));
        return board;
    }

    @Transactional
    @PreAuthorize(
            """
                    !@use.isTheSameUser(#userId) and !@bse.isOwner(#userId, #boardId)
                        and ((@bse.isOwner(#boardId)
                                or (!@bse.isPrivate(#boardId) and @brse.hasBoardRole(#boardId, 'MANAGER')
                                    and !@brse.hasBoardRole(#userId, #boardId, 'MANAGER'))))"""
    )
    public Board removeUserFromBoard(@P("boardId") Long boardId, @P("userId") Long userId) {
        User user = userService.findOne(userId);
        Board board = boardService.removeUser(boardId, user);
        boardRolesService.deleteUserRoles(user, board);
        return board;
    }

    @Transactional
    @PreAuthorize("!@bse.isOwner(#boardId)")
    public Board exitFromBoard(@P("boardId") Long boardId, @P("userId") Long userId) {
        return removeUserFromBoard(boardId, userId);
    }

    public Board addColumn(Long boardId, BoardColumn boardColumn) {
        Board board = boardService.findOne(boardId);
        boardColumn.setBoard(board);
        boardColumnService.save(boardColumn);
        return board;
    }

    @Transactional
    public void deleteColumn(Long columnId) {
        BoardColumn boardColumn = boardColumnService.find(columnId);
        Set<Long> tasksIds = taskService.findByStatusAndBoardId(boardColumn.getStatus(), boardColumn.getBoard().getId())
                .stream()
                .map(BaseEntity::getId)
                .collect(Collectors.toSet());
        taskService.delete(tasksIds);
        boardColumnService.delete(columnId);
    }
}
