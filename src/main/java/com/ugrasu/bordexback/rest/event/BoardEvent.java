package com.ugrasu.bordexback.rest.event;

import com.ugrasu.bordexback.rest.dto.event.BoardEventDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class BoardEvent extends ApplicationEvent {

    private final BoardEventDto boardEventDto;

    public BoardEvent(Object source,BoardEventDto boardEventDto) {
        super(source);
        this.boardEventDto = boardEventDto;
    }
}
