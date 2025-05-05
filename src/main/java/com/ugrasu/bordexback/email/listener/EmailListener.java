package com.ugrasu.bordexback.email.listener;


import com.ugrasu.bordexback.email.mapper.EmailMapper;
import com.ugrasu.bordexback.email.sender.EmailSender;
import com.ugrasu.bordexback.notification.dto.event.NotificationEventDto;
import com.ugrasu.bordexback.notification.event.NotificationEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;


@Async
@RequiredArgsConstructor
@Component
public class EmailListener {

    private final EmailMapper emailMapper;
    private final EmailSender emailSender;

    @EventListener
    public void handleTaskEvent(NotificationEvent notificationEvent) {
        NotificationEventDto notificationEventDto = notificationEvent.getNotificationEventDto();
        emailMapper.toEntities(notificationEventDto)
                .parallelStream()
                .forEach(emailSender::send);
    }
}
