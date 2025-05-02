package com.ugrasu.bordexback.notification.mapper;

import com.ugrasu.bordexback.notification.dto.NotificationDto;
import com.ugrasu.bordexback.notification.entity.Notification;
import com.ugrasu.bordexback.rest.dto.event.TaskEventDto;
import com.ugrasu.bordexback.rest.event.TaskEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface NotificationMapper {
    NotificationDto toDto(TaskEventDto taskEvent);

    Notification toEntity(TaskEventDto taskEvent);
}
