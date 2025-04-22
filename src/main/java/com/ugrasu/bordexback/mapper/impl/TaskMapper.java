package com.ugrasu.bordexback.mapper.impl;

import com.ugrasu.bordexback.dto.full.TaskDto;
import com.ugrasu.bordexback.dto.slim.TaskSlimDto;
import com.ugrasu.bordexback.entity.Task;
import com.ugrasu.bordexback.mapper.Mappable;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface TaskMapper extends Mappable<Task, TaskDto, TaskSlimDto> {

}