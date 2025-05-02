package com.ugrasu.bordexback.notification.listener;

import com.ugrasu.bordexback.notification.entity.Notification;
import com.ugrasu.bordexback.notification.mapper.NotificationMapper;
import com.ugrasu.bordexback.notification.service.NotificationService;
import com.ugrasu.bordexback.rest.event.TaskEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Async
@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationMapper notificationMapper;
    private final NotificationService notificationService;

    @EventListener
    public void handleTaskEvent(TaskEvent taskEvent) {
        Notification notification = notificationMapper.toEntity(taskEvent.getTaskEventDto());
        notification.setEventType(taskEvent.getEventType());
        notificationService.save(notification);
    }
}
