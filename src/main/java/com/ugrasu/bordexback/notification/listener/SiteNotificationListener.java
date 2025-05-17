package com.ugrasu.bordexback.notification.listener;

import com.ugrasu.bordexback.notification.dto.event.NotificationPayload;
import com.ugrasu.bordexback.notification.entity.Consumer;
import com.ugrasu.bordexback.notification.entity.Notification;
import com.ugrasu.bordexback.notification.event.NotificationEvent;
import com.ugrasu.bordexback.notification.mapper.NotificationMapper;
import com.ugrasu.bordexback.notification.publisher.NotificationPublisher;
import com.ugrasu.bordexback.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Async
@Component
@RequiredArgsConstructor
public class SiteNotificationListener {

    private final NotificationService notificationService;
    private final NotificationMapper notificationMapper;
    private final NotificationPublisher notificationPublisher;

    @EventListener
    public void handleTaskEvent(NotificationEvent notificationEvent) {
        NotificationPayload notificationPayload = notificationEvent.getNotificationPayload();
        Notification notification = notificationMapper.toEntity(notificationPayload);
        notification.setConsumers(notification.getConsumers()
                .stream()
                .filter(Consumer::getAllowOnSiteNotifications)
                .collect(Collectors.toSet()));
        notificationService.save(notification);
        notificationPublisher.publishSite(notification);
    }

}
