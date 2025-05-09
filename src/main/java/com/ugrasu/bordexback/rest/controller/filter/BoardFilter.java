package com.ugrasu.bordexback.rest.controller.filter;

import com.ugrasu.bordexback.rest.entity.Board;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.CollectionUtils;

import java.util.Set;

@Data
public class BoardFilter {

    @Schema(description = "ID владельца доски")
    private Long ownerId;

    @Schema(description = "Название доски (поиск по подстроке)")
    private String name;

    @Schema(description = "Описание доски (поиск по подстроке)")
    private String description;

    @Schema(description = "Id пользователей")
    private Set<Long> memberIds;

    public Specification<Board> toSpecification() {
        return Specification.where(ownerIdSpec())
                .and(nameSpec())
                .and(descriptionSpec())
                .and(memberIdsSpec());
    }

    private Specification<Board> ownerIdSpec() {
        return (root, query, cb) -> ownerId != null
                ? cb.equal(root.get("owner").get("id"), ownerId)
                : null;
    }

    private Specification<Board> nameSpec() {
        return (root, query, cb) -> name != null && !name.isEmpty()
                ? cb.like(cb.lower(root.get("name")),  name.toLowerCase() + "%")
                : null;
    }

    private Specification<Board> descriptionSpec() {
        return (root, query, cb) -> description != null && !description.isEmpty()
                ? cb.like(cb.lower(root.get("description")), description.toLowerCase() + "%")
                : null;
    }

    private Specification<Board> memberIdsSpec() {
        return (root, query, cb) -> !CollectionUtils.isEmpty(memberIds)
                ? root.join("boardMembers").get("id").in(memberIds)
                : null;
    }
}
