package com.ugrasu.bordexback.rest.publisher;

import com.ugrasu.bordexback.rest.dto.payload.BoardPayload;
import com.ugrasu.bordexback.rest.dto.payload.TaskPayload;
import com.ugrasu.bordexback.rest.dto.payload.UserPayload;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.BoardRoles;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.event.*;
import com.ugrasu.bordexback.rest.mapper.EventMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final ApplicationEventPublisher eventPublisher;
    private final EventMapper eventMapper;

    private void publish(ApplicationEvent event) {
        eventPublisher.publishEvent(event);
    }

    public Task publish(EventType eventType, Task task) {
        return publish(eventType, task, null);
    }

    public Task publish(EventType eventType, Task task, User unassignUser) {
        TaskPayload taskPayload = eventMapper.toPayload(task, eventType);
        if (unassignUser != null) {
            UserPayload userPayload = eventMapper.toPayload(unassignUser, eventType);
            taskPayload.setUnassignUser(userPayload);
        }
        publish(new TaskEvent(this, taskPayload));
        return task;
    }

    public Board publish(EventType eventType, Board board) {
        return publish(eventType, board, null);
    }

    public Board publish(EventType eventType, Board board, User unassignUser) {
        BoardPayload boardPayload = eventMapper.toPayload(board, eventType);
        if(unassignUser != null) {
            UserPayload userPayload = eventMapper.toPayload(unassignUser, eventType);
            boardPayload.setUnassignUser(userPayload);
        }
        publish(new BoardEvent(this, boardPayload));
        return board;
    }

    public User publish(EventType eventType, User user) {
        UserPayload userPayload = eventMapper.toPayload(user, eventType);
        publish(new UserEvent(this, userPayload));
        return user;
    }

    public BoardRoles publish(EventType eventType, BoardRoles boardRoles) {
        var boardRolePayload = eventMapper.toPayload(boardRoles, eventType);
        publish(new BoardRolesEvent(this, boardRolePayload));
        return boardRoles;
    }
}
