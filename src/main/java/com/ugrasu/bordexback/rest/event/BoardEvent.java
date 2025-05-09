package com.ugrasu.bordexback.rest.event;

import com.ugrasu.bordexback.rest.dto.payload.BoardPayload;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BoardEvent extends ApplicationEvent {

    private final BoardPayload boardPayload;

    public BoardEvent(Object source, BoardPayload boardPayload) {
        super(source);
        this.boardPayload = boardPayload;
    }
}
