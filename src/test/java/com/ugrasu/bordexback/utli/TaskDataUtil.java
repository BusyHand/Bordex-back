package com.ugrasu.bordexback.utli;

import com.ugrasu.bordexback.entity.Task;
import com.ugrasu.bordexback.entity.enums.Priority;
import com.ugrasu.bordexback.entity.enums.Status;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class TaskDataUtil {

    public Task getSimpleTask() {
        return Task.builder()
                .name("Task")
                .description("Task")
                .deadline(LocalDateTime.now().plusDays(1))
                .status(Status.NEW)
                .priority(Priority.MEDIUM)
                .build();

    }
}
