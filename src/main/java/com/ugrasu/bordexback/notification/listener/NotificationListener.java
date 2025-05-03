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


    //todo to user
    // (назначили таск,
    // сняли с таск,
    // изменение таски к которой приписан,
    // довили в доску по username,
    // удалили с доски,
    // сменили роль в доске)
    @EventListener
    public void handleTaskEvent(TaskEvent taskEvent) {
        Notification notification = notificationMapper.toEntity(taskEvent.getTaskEventDto());
        notification.setTitle("TITLE");
        notification.setContent("CONTENT");
        notification.setLink("LINK");
        notificationService.save(notification);
    }
}
