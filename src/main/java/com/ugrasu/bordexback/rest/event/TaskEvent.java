package com.ugrasu.bordexback.rest.event;

import com.ugrasu.bordexback.rest.dto.event.TaskEventDto;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TaskEvent extends ApplicationEvent {

    private final TaskEventDto taskEventDto;

    public TaskEvent(Object source, TaskEventDto task) {
        super(source);
        this.taskEventDto = task;
    }
}
