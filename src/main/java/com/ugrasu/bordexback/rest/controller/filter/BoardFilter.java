package com.ugrasu.bordexback.rest.controller.filter;

import com.ugrasu.bordexback.rest.entity.Board;
import org.springframework.data.jpa.domain.Specification;

public record BoardFilter(Long ownerId, String name, String description) {

    public Specification<Board> toSpecification() {
        return Specification.where(ownerIdSpec())
                .and(nameSpec())
                .and(descriptionSpec());
    }

    private Specification<Board> ownerIdSpec() {
        return (root, query, cb) -> ownerId != null
                ? cb.equal(root.get("owner").get("id"), ownerId)
                : null;
    }

    private Specification<Board> nameSpec() {
        return (root, query, cb) -> name != null && !name.isEmpty()
                ? cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%")
                : null;
    }

    private Specification<Board> descriptionSpec() {
        return (root, query, cb) -> description != null && !description.isEmpty()
                ? cb.like(cb.lower(root.get("description")), "%" + description.toLowerCase() + "%")
                : null;
    }
}
