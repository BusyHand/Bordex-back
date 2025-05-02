package com.ugrasu.bordexback.notification.listener;

import com.ugrasu.bordexback.notification.dto.NotificationDto;
import com.ugrasu.bordexback.notification.mapper.NotificationMapper;
import com.ugrasu.bordexback.rest.dto.event.TaskEventDto;
import com.ugrasu.bordexback.rest.dto.web.full.TaskDto;
import com.ugrasu.bordexback.rest.event.EventType;
import com.ugrasu.bordexback.rest.event.TaskEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.ugrasu.bordexback.rest.event.EventType.TASK_DELETED;
import static com.ugrasu.bordexback.rest.event.EventType.TASK_UNASSIGNED;

@Async
@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationMapper notificationMapper;

    @EventListener
    public void handleTaskEvent(TaskEvent taskEvent) {
        NotificationDto notificationDto = notificationMapper.toNotification(taskEvent);

    }
}
