package com.ugrasu.bordexback.websocket.mapper;

import com.ugrasu.bordexback.notification.dto.event.NotificationPayload;
import com.ugrasu.bordexback.notification.dto.web.NotificationDto;
import com.ugrasu.bordexback.rest.dto.payload.BoardPayload;
import com.ugrasu.bordexback.rest.dto.payload.BoardRolesPayload;
import com.ugrasu.bordexback.rest.dto.payload.TaskPayload;
import com.ugrasu.bordexback.rest.dto.payload.UserPayload;
import com.ugrasu.bordexback.rest.dto.web.full.BoardDto;
import com.ugrasu.bordexback.rest.dto.web.full.BoardRolesDto;
import com.ugrasu.bordexback.rest.dto.web.full.TaskDto;
import com.ugrasu.bordexback.rest.dto.web.full.UserDto;
import com.ugrasu.bordexback.rest.dto.web.slim.BoardRolesSlimDto;
import com.ugrasu.bordexback.rest.dto.web.slim.BoardSlimDto;
import com.ugrasu.bordexback.rest.dto.web.slim.TaskSlimDto;
import com.ugrasu.bordexback.rest.dto.web.slim.UserSlimDto;
import com.ugrasu.bordexback.rest.mapper.impl.TaskMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = TaskMapper.class
)
public interface WebSocketEventMapper {


    BoardDto toDto(BoardPayload board);

    UserDto toDto(UserPayload user);

    BoardRolesDto toDto(BoardRolesPayload userBoardRole);

    NotificationDto toDto(NotificationPayload notificationPayload);

    BoardSlimDto toSlimDto(BoardPayload boardPayload);

    UserSlimDto toSlimDto(UserPayload userPayload);

    BoardRolesSlimDto toSlimDto(BoardRolesPayload boardRolesPayload);

    TaskSlimDto toSlimDto(TaskPayload taskPayload);

    @Mapping(
            target = "assignees",
            expression = "java(mapAssignees(task.getAssignees()))"
    )
    TaskDto toDto(TaskPayload task);

    default Set<UserSlimDto> mapAssignees(Set<UserPayload> assignees) {
        if (assignees == null || assignees.isEmpty()) return Collections.emptySet();
        return assignees.stream()
                .map(this::toSlimDto)
                .collect(Collectors.toSet());
    }


}
