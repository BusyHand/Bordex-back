package com.ugrasu.bordexback.telegram.event;

import com.ugrasu.bordexback.rest.dto.payload.UserPayload;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TelegramUserEvent extends ApplicationEvent {

    private final UserPayload userpayload;

    public TelegramUserEvent(Object source, UserPayload userPayload) {
        super(source);
        this.userpayload = userPayload;
    }
}
