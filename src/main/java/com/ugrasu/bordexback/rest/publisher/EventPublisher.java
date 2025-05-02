package com.ugrasu.bordexback.rest.publisher;

import com.ugrasu.bordexback.rest.dto.event.BoardEventDto;
import com.ugrasu.bordexback.rest.dto.event.TaskEventDto;
import com.ugrasu.bordexback.rest.dto.event.UserEventDto;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.UserBoardRole;
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

    public Task publish(EventType eventType, Task task, long... unassignUserId) {
        TaskEventDto taskEventDto = eventMapper.toEventDto(task);
        if (unassignUserId != null && unassignUserId.length > 0) {
            taskEventDto.setUnassignUserId(unassignUserId[0]);
        }
        publish(new TaskEvent(this, eventType, taskEventDto));
        return task;
    }

    public Board publish(EventType eventType, Board board) {
        BoardEventDto boardEventDto = eventMapper.toEventDto(board);
        publish(new BoardEvent(this, eventType, boardEventDto));
        return board;
    }

    public User publish(EventType eventType, User user) {
        UserEventDto userEventDto = eventMapper.toEventDto(user);
        publish(new UserEvent(this, eventType, userEventDto));
        return user;
    }

    public UserBoardRole publish(EventType eventType, UserBoardRole userBoardRole) {
        var userBoardRoleEventDto = eventMapper.toEventDto(userBoardRole);
        publish(new UserBoardRoleEvent(this, eventType, userBoardRoleEventDto));
        return userBoardRole;
    }
}
