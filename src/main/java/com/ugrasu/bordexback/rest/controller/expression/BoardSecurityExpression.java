package com.ugrasu.bordexback.rest.controller.expression;

import com.ugrasu.bordexback.rest.entity.BaseEntity;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import static com.ugrasu.bordexback.rest.entity.enums.Scope.PRIVATE;

@Component("bse")
@RequiredArgsConstructor
public class BoardSecurityExpression extends SecurityExpressionPrincipal {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardRoleSecurityExpression boardRoleSecurityExpression;

    public boolean canAddUser(Long boardId, Long userId) {
        if (isOwner(getPrincipal().getUserId(), boardId)) {
            return true;
        }
        return !isPrivate(boardId) && boardRoleSecurityExpression.hasBoardRole(userId, boardId, BoardRole.MANAGER.name());
    }

    public boolean canRemoveUser(Long boardId, Long userId) {
        if (isOwner(getPrincipal().getUserId(), boardId)) {
            return true;
        }
        return !isPrivate(boardId) && boardRoleSecurityExpression.hasBoardRole(userId, boardId, BoardRole.MANAGER.name());
    }

    public boolean isOwner(Long boardId) {
        return isOwner(getPrincipal().getUserId(), boardId);
    }

    public boolean isOwner(Long userId, Long boardId) {
        if (userId == null || boardId == null) {
            return false;
        }
        User authUser = userRepository.findById(userId)
                .orElseThrow(() -> new AccessDeniedException("Доступ запрещен"));
        return authUser.getOwnBoards()
                .stream()
                .map(BaseEntity::getId)
                .anyMatch(boardId::equals);
    }

    public boolean isPrivate(Long boardId) {
        if (boardId == null) {
            return false;
        }
        return PRIVATE.equals(boardRepository.findById(boardId)
                .orElseThrow(() -> new AccessDeniedException("Доступ к ресурсу запрещен"))
                .getScope());
    }

    public boolean isMember(Long boardId) {
        if (boardId == null) {
            return false;
        }
        User authUser = userRepository.findById(getPrincipal().getUserId())
                .orElseThrow(() -> new AccessDeniedException("Доступ запрещен"));
        return authUser.getMemberBoards()
                .stream()
                .map(BaseEntity::getId)
                .anyMatch(boardId::equals);
    }
}
