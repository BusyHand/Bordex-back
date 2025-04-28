package com.ugrasu.bordexback.rest.controller.filter;

import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.Role;
import org.springframework.data.jpa.domain.Specification;

public record UserFilter(String username, String email, Role role, Boolean block) {

    public Specification<User> toSpecification() {
        return Specification.where(usernameSpec())
                .and(emailSpec())
                .and(roleSpec())
                .and(blockSpec());
    }

    private Specification<User> usernameSpec() {
        return (root, query, cb) -> username != null
                ? cb.like(cb.lower(root.get("username")), "%" + username.toLowerCase() + "%")
                : null;
    }

    private Specification<User> emailSpec() {
        return (root, query, cb) -> email != null
                ? cb.like(cb.lower(root.get("email")), "%" + email.toLowerCase() + "%")
                : null;
    }

    private Specification<User> roleSpec() {
        return (root, query, cb) -> role != null
                ? cb.isMember(role, root.get("roles"))
                : null;
    }

    private Specification<User> blockSpec() {
        return (root, query, cb) -> block != null
                ? cb.equal(root.get("block"), block)
                : null;
    }
}
