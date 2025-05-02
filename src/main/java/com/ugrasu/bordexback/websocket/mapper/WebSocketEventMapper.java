package com.ugrasu.bordexback.websocket.mapper;

import com.ugrasu.bordexback.rest.dto.event.BoardEventDto;
import com.ugrasu.bordexback.rest.dto.event.TaskEventDto;
import com.ugrasu.bordexback.rest.dto.event.UserBoardRoleEventDto;
import com.ugrasu.bordexback.rest.dto.event.UserEventDto;
import com.ugrasu.bordexback.rest.dto.web.full.BoardDto;
import com.ugrasu.bordexback.rest.dto.web.full.TaskDto;
import com.ugrasu.bordexback.rest.dto.web.full.UserBoardRoleDto;
import com.ugrasu.bordexback.rest.dto.web.full.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface WebSocketEventMapper {
    TaskDto toDto(TaskEventDto task);

    BoardDto toDto(BoardEventDto board);

    UserDto toDto(UserEventDto user);

    UserBoardRoleDto toDto(UserBoardRoleEventDto userBoardRole);


}
