package com.ugrasu.bordexback.notification.event;


import com.ugrasu.bordexback.notification.dto.event.NotificationPayload;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationSiteEvent extends ApplicationEvent {

    private final NotificationPayload notificationPayload;

    public NotificationSiteEvent(Object source, NotificationPayload notificationPayload) {
        super(source);
        this.notificationPayload = notificationPayload;
    }
}
