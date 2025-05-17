package com.ugrasu.bordexback.rest.controller.filter;

import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.Role;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public record UserFilter(String username, String telegramUsername, String email, Role role, Boolean block,
                         List<Long> boardIds) {

    public Specification<User> filter() {
        return Specification
                .where(usernameSpec(username))
                .or(telegramUsername(telegramUsername))
                .or(emailSpec(email))
                .and(roleSpec(role))
                .and(blockSpec(block))
                .and(userBoardsIdInSpec(boardIds));
    }

    private Specification<User> usernameSpec(String username) {
        return (root, query, cb) -> username != null
                ? cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%")
                : null;
    }

    private Specification<User> emailSpec(String email) {
        return (root, query, cb) -> email != null
                ? cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%")
                : null;
    }

    private Specification<User> telegramUsername(String telegramUsername) {
        return (root, query, cb) -> telegramUsername != null
                ? cb.like(cb.lower(root.get("telegramUsername")), "%" + telegramUsername.toLowerCase() + "%")
                : null;
    }

    private Specification<User> roleSpec(Role role) {
        return (root, query, cb) -> role != null
                ? cb.isMember(role, root.get("roles"))
                : null;
    }

    private Specification<User> blockSpec(Boolean block) {
        return (root, query, cb) -> block != null
                ? cb.equal(root.get("block"), block)
                : null;
    }

    private Specification<User> userBoardsIdInSpec(List<Long> boardIds) {
        return ((root, query, cb) -> boardIds != null
                ? root.get("memberBoards").get("id").in(boardIds)
                : null);
    }
}
