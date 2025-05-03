package com.ugrasu.bordexback.rest.mapper;

import com.ugrasu.bordexback.rest.dto.event.BoardEventDto;
import com.ugrasu.bordexback.rest.dto.event.TaskEventDto;
import com.ugrasu.bordexback.rest.dto.event.UserBoardRoleEventDto;
import com.ugrasu.bordexback.rest.dto.event.UserEventDto;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.UserBoardRole;
import com.ugrasu.bordexback.rest.event.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface EventMapper {

    @Mapping(
            target = "tagColor",
            expression = "java(task.getTag() != null ? task.getTag().getColor() : null)"
    )
    TaskEventDto toEventDto(Task task, EventType eventType);

    BoardEventDto toEventDto(Board board, EventType eventType);

    UserEventDto toEventDto(User user, EventType eventType);

    UserBoardRoleEventDto toEventDto(UserBoardRole userBoardRole, EventType eventType);


}
