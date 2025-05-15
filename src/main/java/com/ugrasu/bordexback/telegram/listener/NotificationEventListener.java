package com.ugrasu.bordexback.telegram.listener;

import com.ugrasu.bordexback.notification.dto.event.ConsumerPayload;
import com.ugrasu.bordexback.notification.dto.event.NotificationPayload;
import com.ugrasu.bordexback.notification.event.NotificationEvent;
import com.ugrasu.bordexback.telegram.sender.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Async
@Component
@RequiredArgsConstructor
public class NotificationEventListener {

    private final MessageSender messageSender;

    @EventListener
    public void handleTaskEvent(NotificationEvent notificationEvent) {
        NotificationPayload notificationPayload = notificationEvent.getNotificationPayload();
        String title = notificationPayload.getTitle();
        String content = notificationPayload.getContent();
        List<Long> chatIds = notificationPayload.getConsumers()
                .stream()
                .filter(ConsumerPayload::getAllowTelegramNotifications)
                .map(ConsumerPayload::getChatId)
                .filter(Objects::nonNull)
                .toList();
        messageSender.sendMessage(chatIds, title + "\n" + content);
    }
}
