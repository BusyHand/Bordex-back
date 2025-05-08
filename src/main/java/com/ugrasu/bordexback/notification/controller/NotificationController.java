package com.ugrasu.bordexback.notification.controller;

import com.ugrasu.bordexback.notification.controller.filter.NotificationFilter;
import com.ugrasu.bordexback.notification.dto.web.NotificationDto;
import com.ugrasu.bordexback.notification.entity.Notification;
import com.ugrasu.bordexback.notification.mapper.NotificationMapper;
import com.ugrasu.bordexback.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@PreAuthorize("isAuthenticated()")
public class NotificationController {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;

    @GetMapping
    public PagedModel<NotificationDto> findAll(@ParameterObject @ModelAttribute NotificationFilter filter,
                                               @ParameterObject Pageable pageable) {
        Specification<Notification> specification = filter.toSpecification();
        Page<Notification> notifications = notificationService.findAll(specification, pageable);
        Page<NotificationDto> notificationDtos = notifications.map(notificationMapper::toDto);
        return new PagedModel<>(notificationDtos);
    }

    @DeleteMapping("/{notification-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("notification-id") Long notificationId,
                       @RequestParam("userId") Long userId) {
        notificationService.delete(notificationId, userId);
    }
}
