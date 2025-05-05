package com.ugrasu.bordexback.notification.mapper;

import com.ugrasu.bordexback.notification.dto.event.ConsumerEventDto;
import com.ugrasu.bordexback.notification.dto.event.NotificationEventDto;
import com.ugrasu.bordexback.notification.dto.web.NotificationDto;
import com.ugrasu.bordexback.notification.entity.Consumer;
import com.ugrasu.bordexback.notification.entity.Notification;
import com.ugrasu.bordexback.rest.dto.event.TaskEventDto;
import com.ugrasu.bordexback.rest.dto.web.slim.UserSlimDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
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

    ConsumerEventDto toEventDto(Consumer consumer);

    @Mapping(
            target = "id",
            ignore = true
    )
    @Mapping(
            target = "consumers",
            expression = "java(mapConsumers(taskEventDto))"
    )
    Notification toEntity(TaskEventDto taskEventDto);

    default Set<Consumer> mapConsumers(TaskEventDto taskEventDto) {
        Set<UserSlimDto> userSlimDtos = taskEventDto.getAssignees();
        UserSlimDto unassignUser = taskEventDto.getUnassignUser();
        if (unassignUser != null) {
            userSlimDtos.add(unassignUser);
        }
        if (userSlimDtos == null) {
            return Collections.emptySet();
        }
        return userSlimDtos.stream()
                .map(userSlimDto -> {
                    Consumer userConsumerEventDto = new Consumer();
                    userConsumerEventDto.setUsername(userSlimDto.getUsername());
                    userConsumerEventDto.setUserId(userSlimDto.getId());
                    userConsumerEventDto.setEmail(userSlimDto.getEmail());
                    userConsumerEventDto.setFirstName(userSlimDto.getFirstName());
                    userConsumerEventDto.setLastName(userSlimDto.getLastName());
                    return userConsumerEventDto;
                })
                .collect(Collectors.toSet());
    }
}
