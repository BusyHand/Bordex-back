package com.ugrasu.bordexback.notification.publisher;

import com.ugrasu.bordexback.notification.dto.event.NotificationPayload;
import com.ugrasu.bordexback.notification.entity.Notification;
import com.ugrasu.bordexback.notification.event.NotificationEvent;
import com.ugrasu.bordexback.notification.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationPublisher {

    private final NotificationMapper notificationMapper;
    private final ApplicationEventPublisher eventPublisher;

    public Notification publish(Notification notification) {
        NotificationPayload eventDto = notificationMapper.toEventDto(notification);
        eventPublisher.publishEvent(new NotificationEvent(this, eventDto));
        return notification;
    }
}
