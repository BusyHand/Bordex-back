package com.ugrasu.bordexback.telegram.publisher;

import com.ugrasu.bordexback.rest.dto.payload.UserPayload;
import com.ugrasu.bordexback.rest.event.EventType;
import com.ugrasu.bordexback.telegram.event.TelegramUserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BotEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publish(EventType eventType, UserPayload userPayload) {
        userPayload.setEventType(eventType);
        eventPublisher.publishEvent(new TelegramUserEvent(this, userPayload));
    }
}
