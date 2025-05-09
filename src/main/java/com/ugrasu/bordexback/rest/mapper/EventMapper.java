package com.ugrasu.bordexback.rest.mapper;

import com.ugrasu.bordexback.rest.dto.payload.BoardPayload;
import com.ugrasu.bordexback.rest.dto.payload.BoardRolesPayload;
import com.ugrasu.bordexback.rest.dto.payload.TaskPayload;
import com.ugrasu.bordexback.rest.dto.payload.UserPayload;
import com.ugrasu.bordexback.rest.dto.web.full.BoardDto;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.BoardRoles;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
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
    TaskPayload toPayload(Task task, EventType eventType);

    @Mapping(
            target = "tasksCount",
            expression = "java(countTasks(board))"
    )
    @Mapping(
            target = "membersCount",
            expression = "java(countMembers(board))"
    )
    BoardPayload toPayload(Board board, EventType eventType);

    UserPayload toPayload(User user, EventType eventType);

    BoardRolesPayload toPayload(BoardRoles boardRoles, EventType eventType);

    BoardDto toDto(Board board);

    default Long countTasks(Board board) {
        return board.getTasks() != null ? (long) board.getTasks().size() : 0L;
    }

    default Long countMembers(Board board) {
        return board.getBoardMembers() != null ? (long) board.getBoardMembers().size() : 0L;
    }

}
