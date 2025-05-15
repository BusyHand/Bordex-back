package com.ugrasu.bordexback.rest.controller.filter;

import com.ugrasu.bordexback.rest.entity.Board;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public record BoardFilter(Long ownerId, String name, String description, Set<Long> memberIds) {

    public Specification<Board> filter() {
        return Specification.where(ownerIdSpec(ownerId))
                .and(nameSpec(name))
                .and(descriptionSpec(description))
                .and(memberIdsSpec(memberIds));
    }

    private Specification<Board> ownerIdSpec(Long ownerId) {
        return (root, query, cb) -> ownerId != null
                ? cb.equal(root.get("owner").get("id"), ownerId)
                : null;
    }

    private Specification<Board> nameSpec(String name) {
        return (root, query, cb) -> name != null && !name.isEmpty()
                ? cb.like(cb.lower(root.get("name")), name.toLowerCase() + "%")
                : null;
    }

    private Specification<Board> descriptionSpec(String description) {
        return (root, query, cb) -> description != null && !description.isEmpty()
                ? cb.like(cb.lower(root.get("description")), description.toLowerCase() + "%")
                : null;
    }

    private Specification<Board> memberIdsSpec(Set<Long> memberIds) {
        return (root, query, cb) -> !CollectionUtils.isEmpty(memberIds)
                ? root.join("boardMembers").get("id").in(memberIds)
                : null;
    }
}
