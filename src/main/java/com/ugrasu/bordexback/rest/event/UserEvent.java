package com.ugrasu.bordexback.rest.event;

import com.ugrasu.bordexback.rest.dto.event.UserEventDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserEvent extends ApplicationEvent {

    private final UserEventDto userEventDto;
    private final EventType eventType;

    public UserEvent(Object source, EventType eventType, UserEventDto userEventDto) {
        super(source);
        this.userEventDto = userEventDto;
        this.eventType = eventType;
    }
}
