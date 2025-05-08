package com.ugrasu.bordexback.notification.controller.filter;

import com.ugrasu.bordexback.notification.entity.Notification;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class NotificationFilter {

    @Schema(description = "ID потребителя userId")
    private Long userId;

    public Specification<Notification> toSpecification() {
        return Specification.where(consumerIdSpec());
    }

    private Specification<Notification> consumerIdSpec() {
        return (root, query, cb) -> userId != null
                ? cb.equal(root.join("consumers").get("userId"), userId)
                : null;
    }
}
