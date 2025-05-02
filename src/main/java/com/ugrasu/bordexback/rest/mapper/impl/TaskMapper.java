package com.ugrasu.bordexback.rest.mapper.impl;

import com.ugrasu.bordexback.rest.dto.web.full.TaskDto;
import com.ugrasu.bordexback.rest.dto.web.slim.TaskSlimDto;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.mapper.WebMappable;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface TaskMapper extends WebMappable<Task, TaskDto, TaskSlimDto> {

    @Mapping(
            target = "tagColor",
            expression = "java(task.getTag() != null ? task.getTag().getColor() : null)"
    )
    TaskDto toDto(Task task);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(
            target = "assignees",
            ignore = true
    )
    Task partialUpdate(Task updatedTask, @MappingTarget Task oldTask);
}