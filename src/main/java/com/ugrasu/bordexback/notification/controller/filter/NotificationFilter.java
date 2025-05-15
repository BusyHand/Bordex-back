package com.ugrasu.bordexback.notification.controller.filter;

import com.ugrasu.bordexback.notification.entity.Notification;
import lombok.experimental.UtilityClass;
import org.springframework.data.jpa.domain.Specification;

public record NotificationFilter(Long userId) {

    public Specification<Notification> filter() {
        return Specification.where(consumerIdSpec(userId));
    }

    private Specification<Notification> consumerIdSpec(Long userId) {
        return (root, query, cb) -> userId != null
                ? cb.equal(root.join("consumers").get("userId"), userId)
                : null;
    }
}
