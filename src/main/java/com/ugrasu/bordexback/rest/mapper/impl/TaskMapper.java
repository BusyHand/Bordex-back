package com.ugrasu.bordexback.rest.mapper.impl;

import com.ugrasu.bordexback.rest.dto.full.TaskDto;
import com.ugrasu.bordexback.rest.dto.slim.TaskSlimDto;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.mapper.Mappable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper extends Mappable<Task, TaskDto, TaskSlimDto> {

    @Mapping(target = "tagColor", expression = "java(task.getTag() != null ? task.getTag().getColor() : null)")
    TaskDto toDto(Task task);
}