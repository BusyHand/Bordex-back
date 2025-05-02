package com.ugrasu.bordexback.rest.event;

import com.ugrasu.bordexback.rest.dto.event.UserBoardRoleEventDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserBoardRoleEvent extends ApplicationEvent {

    private final UserBoardRoleEventDto userBoardRoleEventDto;
    private final EventType eventType;

    public UserBoardRoleEvent(Object source, EventType eventType, UserBoardRoleEventDto userBoardRoleEventDto) {
        super(source);
        this.userBoardRoleEventDto = userBoardRoleEventDto;
        this.eventType = eventType;
    }
}
