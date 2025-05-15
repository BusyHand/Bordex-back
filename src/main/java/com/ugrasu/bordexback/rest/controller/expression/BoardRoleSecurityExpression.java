package com.ugrasu.bordexback.rest.controller.expression;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.BoardRoles;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.BoardRolesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component("brse")
@RequiredArgsConstructor
public class BoardRoleSecurityExpression extends SecurityExpressionPrincipal {

    private final BoardRolesRepository boardRolesRepository;
    private final BoardRepository boardRepository;

    public boolean hasBoardRole(Long boardId, String role) {
        if (boardId == null || role == null) {
            return false;
        }
        BoardRoles userBoardRoles = boardRolesRepository.findByUser_IdAndBoard_Id(getPrincipal().getUserId(), boardId)
                .orElseThrow(() -> new AccessDeniedException("Доступ запрещен"));
        Set<BoardRole> boardRoles = userBoardRoles.getBoardRoles();
        return boardRoles.stream()
                .anyMatch(boardRole -> boardRole.name().equals(role));
    }

    public boolean hasBoardRole(Long userId, Long boardId, String role) {
        if (boardId == null || role == null) {
            return false;
        }
        BoardRoles userBoardRoles = boardRolesRepository.findByUser_IdAndBoard_Id(userId, boardId)
                .orElseThrow(() -> new AccessDeniedException("Доступ запрещен"));
        Set<BoardRole> boardRoles = userBoardRoles.getBoardRoles();
        return boardRoles.stream()
                .anyMatch(boardRole -> boardRole.name().equals(role));
    }

    public boolean canChange(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new AccessDeniedException("Доступ запрещен"));
        Long authUserId = getPrincipal().getUserId();
        if (board.getOwner().getId().equals(authUserId)) {
            return true;
        }
        BoardRoles userBoardRoles = boardRolesRepository.findByUser_IdAndBoard_Id(authUserId, boardId)
                .orElseThrow(() -> new AccessDeniedException("Доступ запрещен"));
        Set<BoardRole> boardRoles = userBoardRoles.getBoardRoles();
        return !boardRoles.contains(BoardRole.MANAGER);
    }

    public boolean allowToUpdateBoardRole(Long userId, Long boardId, Set<BoardRole> newRoles) {
        if (userId == null || boardId == null || newRoles == null) {
            return false;
        }
        Set<BoardRole> currentRoles = boardRolesRepository.findByUser_IdAndBoard_Id(userId, boardId)
                .orElseThrow(() -> new AccessDeniedException("Доступ запрещен"))
                .getBoardRoles();
        boolean wasManager = currentRoles.contains(BoardRole.MANAGER);
        boolean willBeManager = newRoles.contains(BoardRole.MANAGER);
        return !wasManager || willBeManager;
    }

    public boolean allowToDeleteRole(Long userId, Long boardId, BoardRole boardRole) {
        if (userId == null || boardId == null || boardRole == null) {
            return false;
        }
        Set<BoardRole> oldBoardRoles = boardRolesRepository.findByUser_IdAndBoard_Id(userId, boardId)
                .orElseThrow(() -> new AccessDeniedException("Доступ запрещен"))
                .getBoardRoles();
        return oldBoardRoles.contains(BoardRole.MANAGER) && !boardRole.equals(BoardRole.MANAGER);
    }

}
