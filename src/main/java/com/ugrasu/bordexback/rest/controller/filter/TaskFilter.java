package com.ugrasu.bordexback.rest.controller.filter;

import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.enums.Priority;
import com.ugrasu.bordexback.rest.entity.enums.Status;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.Set;

public record TaskFilter(Long boardId, Set<Long> assigneeIds, Status status, Priority priority) {

    public Specification<Task> toSpecification() {
        return Specification.where(boardIdSpec())
                .and(assigneeSpec())
                .and(statusSpec())
                .and(prioritySpec());
    }

    private Specification<Task> boardIdSpec() {
        return (root, query, cb) -> boardId != null
                ? cb.equal(root.get("board").get("id"), boardId)
                : null;
    }

    private Specification<Task> assigneeSpec() {
        return (root, query, cb) -> !CollectionUtils.isEmpty(assigneeIds)
                ? root.join("assignees").get("id").in(assigneeIds)
                : null;
    }

    private Specification<Task> statusSpec() {
        return (root, query, cb) -> status != null
                ? cb.equal(root.get("status"), status)
                : null;
    }

    private Specification<Task> prioritySpec() {
        return (root, query, cb) -> priority != null
                ? cb.equal(root.get("priority"), priority)
                : null;
    }
}
