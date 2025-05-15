package com.ugrasu.bordexback.notification.mapper;

import com.ugrasu.bordexback.notification.dto.event.ConsumerPayload;
import com.ugrasu.bordexback.notification.dto.event.NotificationPayload;
import com.ugrasu.bordexback.notification.dto.web.NotificationDto;
import com.ugrasu.bordexback.notification.entity.Consumer;
import com.ugrasu.bordexback.notification.entity.Notification;
import com.ugrasu.bordexback.rest.dto.payload.BoardPayload;
import com.ugrasu.bordexback.rest.dto.payload.BoardRolesPayload;
import com.ugrasu.bordexback.rest.dto.payload.TaskPayload;
import com.ugrasu.bordexback.rest.dto.payload.UserPayload;
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

    NotificationDto toDto(TaskPayload taskEvent);

    NotificationDto toDto(Notification notification);

    NotificationPayload toEventDto(Notification notification);

    Notification toEntity(NotificationPayload notificationPayload);

    ConsumerPayload toEventDto(Consumer consumer);

    @Mapping(
            target = "id",
            ignore = true
    )
    @Mapping(
            target = "consumers",
            expression = "java(mapConsumers(taskPayload))"
    )
    Notification toEntity(TaskPayload taskPayload);

    @Mapping(
            target = "id",
            ignore = true
    )
    @Mapping(
            target = "consumers",
            expression = "java(mapConsumers(boardPayload))"
    )
    Notification toEntity(BoardPayload boardPayload);

    @Mapping(
            target = "id",
            ignore = true
    )
    @Mapping(
            target = "consumers",
            expression = "java(mapConsumers(boardRolesPayload))"
    )
    Notification toEntity(BoardRolesPayload boardRolesPayload);

    default Set<Consumer> mapConsumers(TaskPayload taskPayload) {
        Set<UserPayload> userSlimDtos = taskPayload.getAssignees();
        UserPayload unassignUser = taskPayload.getUnassignUser();
        if (unassignUser != null) {
            userSlimDtos.add(unassignUser);
        }
        if (userSlimDtos == null) {
            return Collections.emptySet();
        }
        return userSlimDtos.stream()
                .map(this::mapUserToConsumer)
                .collect(Collectors.toSet());
    }

    default Set<Consumer> mapConsumers(BoardPayload boardPayload) {
        Set<UserPayload> boardMembers = boardPayload.getBoardMembers();
        UserPayload unassignUser = boardPayload.getUnassignUser();
        if (unassignUser != null) {
            boardMembers.add(unassignUser);
        }
        if (boardMembers == null) {
            return Collections.emptySet();
        }
        return boardMembers.stream()
                .map(this::mapUserToConsumer)
                .collect(Collectors.toSet());
    }

    default Set<Consumer> mapConsumers(BoardRolesPayload boardRolesPayload) {
        UserPayload user = boardRolesPayload.getUser();
        if (user == null) {
            return Collections.emptySet();
        }
        return Set.of(mapUserToConsumer(user));
    }

    default Set<Consumer> mapConsumers(UserPayload userPayload) {
        if (userPayload == null) {
            return Collections.emptySet();
        }
        return Set.of(mapUserToConsumer(userPayload));
    }

    default Consumer mapUserToConsumer(UserPayload user) {
        Consumer consumer = new Consumer();
        consumer.setUsername(user.getUsername());
        consumer.setUserId(user.getId());
        consumer.setEmail(user.getEmail());
        consumer.setFirstName(user.getFirstName());
        consumer.setLastName(user.getLastName());
        consumer.setChatId(user.getChatId());
        consumer.setAllowTelegramNotifications(user.getAllowTelegramNotifications());
        consumer.setAllowEmailNotifications(user.getAllowEmailNotifications());
        consumer.setAllowOnSiteNotifications(user.getAllowOnSiteNotifications());
        return consumer;
    }
}
