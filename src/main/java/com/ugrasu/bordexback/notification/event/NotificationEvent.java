package com.ugrasu.bordexback.notification.event;


import com.ugrasu.bordexback.notification.dto.NotificationEventDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationEvent extends ApplicationEvent {

    private final NotificationEventDto notificationEventDto;

    public NotificationEvent(Object source, NotificationEventDto notificationEventDto) {
        super(source);
        this.notificationEventDto = notificationEventDto;
    }
}
