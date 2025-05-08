package com.ugrasu.bordexback.rest.facade;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import com.ugrasu.bordexback.rest.service.BoardRolesService;
import com.ugrasu.bordexback.rest.service.BoardService;
import com.ugrasu.bordexback.rest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class BoardFacadeManagement {

    private final UserService userService;
    private final BoardRolesService boardRolesService;
    private final BoardService boardService;

    @Transactional
    public Board ownerTransfer(Long boardId, Long newOwnerId) {
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
    public Board addUserToBoard(Long boardId, Long userId) {
        User user = userService.findOne(userId);
        Board board = boardService.addUser(boardId, user);
        boardRolesService.save(user, board, Set.of(BoardRole.VIEWER));
        return board;
    }

    @Transactional
    public Board removeUserFromBoard(Long boardId, Long userId) {
        User user = userService.findOne(userId);
        Board board = boardService.removeUser(boardId, user);
        boardRolesService.deleteUserRoles(user, board);
        return board;
    }

}
