package com.ugrasu.bordexback.rest.event;

import com.ugrasu.bordexback.rest.dto.event.UserBoardRoleEventDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserBoardRoleEvent extends ApplicationEvent {

    private final UserBoardRoleEventDto userBoardRoleEventDto;

    public UserBoardRoleEvent(Object source, UserBoardRoleEventDto userBoardRoleEventDto) {
        super(source);
        this.userBoardRoleEventDto = userBoardRoleEventDto;
    }
}
