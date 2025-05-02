package com.ugrasu.bordexback.rest.event;

import com.ugrasu.bordexback.rest.dto.event.TaskEventDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TaskEvent extends ApplicationEvent {

    private final TaskEventDto taskEventDto;
    private final EventType eventType;

    public TaskEvent(Object source, EventType eventType, TaskEventDto task) {
        super(source);
        this.taskEventDto = task;
        this.eventType = eventType;
    }
}
