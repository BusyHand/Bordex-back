package com.ugrasu.bordexback.rest.event;

import com.ugrasu.bordexback.rest.dto.payload.UserPayload;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserEvent extends ApplicationEvent {

    private final UserPayload userPayload;

    public UserEvent(Object source, UserPayload userPayload) {
        super(source);
        this.userPayload = userPayload;
    }
}
