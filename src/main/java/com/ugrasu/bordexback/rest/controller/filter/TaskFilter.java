package com.ugrasu.bordexback.rest.controller.filter;

import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.enums.Priority;
import com.ugrasu.bordexback.rest.entity.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public record TaskFilter(Long boardId, String name, String description, Set<Long> assigneeIds, Status status,
                         Priority priority) {

    public Specification<Task> filter() {
        return Specification.where(boardIdSpec(boardId))
                .and(nameSpec(name))
                .and(descriptionSpec(description))
                .and(assigneeSpec(assigneeIds))
                .and(statusSpec(status))
                .and(prioritySpec(priority));
    }

    private Specification<Task> nameSpec(String name) {
        return (root, query, cb) -> name != null && !name.isEmpty()
                ? cb.like(cb.lower(root.get("name")), name.toLowerCase() + "%")
                : null;
    }

    private Specification<Task> descriptionSpec(String description) {
        return (root, query, cb) -> description != null && !description.isEmpty()
                ? cb.like(cb.lower(root.get("description")), description.toLowerCase() + "%")
                : null;
    }

    private Specification<Task> boardIdSpec(Long boardId) {
        return (root, query, cb) -> boardId != null
                ? cb.equal(root.get("board").get("id"), boardId)
                : null;
    }

    private Specification<Task> assigneeSpec(Set<Long> assigneeIds) {
        return (root, query, cb) -> !CollectionUtils.isEmpty(assigneeIds)
                ? root.join("assignees").get("id").in(assigneeIds)
                : null;
    }

    private Specification<Task> statusSpec(Status status) {
        return (root, query, cb) -> status != null
                ? cb.equal(root.get("status"), status)
                : null;
    }

    private Specification<Task> prioritySpec(Priority priority) {
        return (root, query, cb) -> priority != null
                ? cb.equal(root.get("priority"), priority)
                : null;
    }
}
