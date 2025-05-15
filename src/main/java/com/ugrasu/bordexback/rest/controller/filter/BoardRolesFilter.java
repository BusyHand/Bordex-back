package com.ugrasu.bordexback.rest.controller.filter;

import com.ugrasu.bordexback.rest.entity.BoardRoles;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

public record BoardRolesFilter(Long userId, Long boardId, BoardRole boardRole) {

    public Specification<BoardRoles> filter() {
        return Specification.where(userIdSpec(userId))
                .and(boardIdSpec(boardId))
                .and(boardRoleSpec(boardRole));
    }

    private Specification<BoardRoles> userIdSpec(Long userId) {
        return (root, query, cb) -> userId != null
                ? cb.equal(root.get("user").get("id"), userId)
                : null;
    }

    private Specification<BoardRoles> boardIdSpec(Long boardId) {
        return (root, query, cb) -> boardId != null
                ? cb.equal(root.get("board").get("id"), boardId)
                : null;
    }

    private Specification<BoardRoles> boardRoleSpec(BoardRole boardRole) {
        return (root, query, cb) -> boardRole != null
                ? cb.isMember(boardRole, root.get("boardRoles"))
                : null;
    }
}
