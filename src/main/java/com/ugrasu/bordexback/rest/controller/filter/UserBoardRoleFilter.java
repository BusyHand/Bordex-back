package com.ugrasu.bordexback.rest.controller.filter;

import com.ugrasu.bordexback.rest.entity.UserBoardRole;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import org.springframework.data.jpa.domain.Specification;

public record UserBoardRoleFilter(Long userId, Long boardId, BoardRole boardRole) {

    public Specification<UserBoardRole> toSpecification() {
        return Specification.where(userIdSpec())
                .and(boardIdSpec())
                .and(boardRoleSpec());
    }

    private Specification<UserBoardRole> userIdSpec() {
        return (root, query, cb) -> userId != null
                ? cb.equal(root.get("user").get("id"), userId)
                : null;
    }

    private Specification<UserBoardRole> boardIdSpec() {
        return (root, query, cb) -> boardId != null
                ? cb.equal(root.get("board").get("id"), boardId)
                : null;
    }

    private Specification<UserBoardRole> boardRoleSpec() {
        return (root, query, cb) -> boardRole != null
                ? cb.isMember(boardRole, root.get("boardRoles"))
                : null;
    }
}
