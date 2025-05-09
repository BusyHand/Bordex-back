package com.ugrasu.bordexback.rest.event;

import com.ugrasu.bordexback.rest.dto.payload.BoardRolesPayload;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BoardRolesEvent extends ApplicationEvent {

    private final BoardRolesPayload boardRolePayload;

    public BoardRolesEvent(Object source, BoardRolesPayload boardRolePayload) {
        super(source);
        this.boardRolePayload = boardRolePayload;
    }
}
