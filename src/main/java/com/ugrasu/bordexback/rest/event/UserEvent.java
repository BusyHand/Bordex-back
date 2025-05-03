package com.ugrasu.bordexback.rest.event;

import com.ugrasu.bordexback.rest.dto.event.UserEventDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserEvent extends ApplicationEvent {

    private final UserEventDto userEventDto;

    public UserEvent(Object source, UserEventDto userEventDto) {
        super(source);
        this.userEventDto = userEventDto;
    }
}
