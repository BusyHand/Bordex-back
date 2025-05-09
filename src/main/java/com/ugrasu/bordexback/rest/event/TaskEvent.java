package com.ugrasu.bordexback.rest.event;

import com.ugrasu.bordexback.rest.dto.payload.TaskPayload;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class TaskEvent extends ApplicationEvent {

    private final TaskPayload taskPayload;

    public TaskEvent(Object source, TaskPayload task) {
        super(source);
        this.taskPayload = task;
    }
}
