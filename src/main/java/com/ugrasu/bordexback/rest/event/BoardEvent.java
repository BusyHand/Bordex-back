package com.ugrasu.bordexback.rest.event;

import com.ugrasu.bordexback.rest.dto.event.BoardEventDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BoardEvent extends ApplicationEvent {

    private final BoardEventDto boardEventDto;
    private final EventType eventType;

    public BoardEvent(Object source, EventType eventType, BoardEventDto boardEventDto) {
        super(source);
        this.boardEventDto = boardEventDto;
        this.eventType = eventType;
    }
}
