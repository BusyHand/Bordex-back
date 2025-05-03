package com.ugrasu.bordexback.notification.mapper;

import com.ugrasu.bordexback.notification.dto.NotificationDto;
import com.ugrasu.bordexback.notification.dto.NotificationEventDto;
import com.ugrasu.bordexback.notification.entity.Notification;
import com.ugrasu.bordexback.rest.dto.event.TaskEventDto;
import com.ugrasu.bordexback.rest.dto.web.slim.UserSlimDto;
import com.ugrasu.bordexback.rest.event.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface NotificationMapper {

    NotificationDto toDto(TaskEventDto taskEvent);

    NotificationDto toDto(Notification notification);

    NotificationEventDto toEventDto(Notification notification);

    @Mapping(
            target = "id",
            ignore = true
    )
    @Mapping(
            target = "consumersIds",
            expression = "java(collectConsumers(taskEventDto))"
    )
    Notification toEntity(TaskEventDto taskEventDto);

    default Set<Long> collectConsumers(TaskEventDto taskEventDto) {
        Set<UserSlimDto> assignees = taskEventDto.getAssignees();
        Set<Long> consumersIds = new HashSet<>();
        if (assignees != null && !assignees.isEmpty()) {
            consumersIds.addAll(assignees.stream()
                    .map(UserSlimDto::getId)
                    .collect(Collectors.toSet()));
        }
        Long unassignUserId = taskEventDto.getUnassignUserId();
        if (unassignUserId != null) {
            consumersIds.add(unassignUserId);
        }
        return consumersIds;
    }

}
