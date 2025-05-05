package com.ugrasu.bordexback.websocket.mapper;

import com.ugrasu.bordexback.notification.dto.event.ConsumerEventDto;
import com.ugrasu.bordexback.notification.dto.web.NotificationDto;
import com.ugrasu.bordexback.notification.dto.event.NotificationEventDto;
import com.ugrasu.bordexback.rest.dto.event.BoardEventDto;
import com.ugrasu.bordexback.rest.dto.event.TaskEventDto;
import com.ugrasu.bordexback.rest.dto.event.UserBoardRoleEventDto;
import com.ugrasu.bordexback.rest.dto.event.UserEventDto;
import com.ugrasu.bordexback.rest.dto.web.full.BoardDto;
import com.ugrasu.bordexback.rest.dto.web.full.TaskDto;
import com.ugrasu.bordexback.rest.dto.web.full.UserBoardRoleDto;
import com.ugrasu.bordexback.rest.dto.web.full.UserDto;
import com.ugrasu.bordexback.rest.dto.web.slim.UserSlimDto;
import com.ugrasu.bordexback.rest.mapper.impl.TaskMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = TaskMapper.class
)
public interface WebSocketEventMapper {


    BoardDto toDto(BoardEventDto board);

    UserDto toDto(UserEventDto user);

    UserBoardRoleDto toDto(UserBoardRoleEventDto userBoardRole);

    @Mapping(
            target = "assignees",
            expression = "java(safeAssignees(task.getAssignees()))"
    )
    TaskDto toDto(TaskEventDto task);

    NotificationDto toDto(NotificationEventDto notificationEventDto);

    default Set<UserSlimDto> safeAssignees(Set<UserSlimDto> assignees) {
        if (assignees == null) return Collections.emptySet();
        return CollectionUtils.isEmpty(assignees) ? Collections.emptySet() : assignees;
    }

}
