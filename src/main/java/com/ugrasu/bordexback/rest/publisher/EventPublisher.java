package com.ugrasu.bordexback.rest.publisher;

import com.ugrasu.bordexback.rest.dto.event.BoardEventDto;
import com.ugrasu.bordexback.rest.dto.event.TaskEventDto;
import com.ugrasu.bordexback.rest.dto.event.UserEventDto;
import com.ugrasu.bordexback.rest.dto.web.slim.UserSlimDto;
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

    public Task publish(EventType eventType, Task task, User... unassignUser) {
        TaskEventDto taskEventDto = eventMapper.toEventDto(task, eventType);
        if (unassignUser != null && unassignUser.length > 0) {
            UserSlimDto userSlimDto = new UserSlimDto();
            userSlimDto.setId(unassignUser[0].getId());
            userSlimDto.setUsername(unassignUser[0].getUsername());
            userSlimDto.setEmail(unassignUser[0].getEmail());
            userSlimDto.setFirstName(unassignUser[0].getFirstName());
            userSlimDto.setLastName(unassignUser[0].getLastName());
            taskEventDto.setUnassignUser(userSlimDto);
        }
        publish(new TaskEvent(this, taskEventDto));
        return task;
    }

    //todo
    public Board publish(EventType eventType, Board board) {
        BoardEventDto boardEventDto = eventMapper.toEventDto(board, eventType);
        //publish(new BoardEvent(this, boardEventDto));
        return board;
    }

    public User publish(EventType eventType, User user) {
        UserEventDto userEventDto = eventMapper.toEventDto(user, eventType);
        publish(new UserEvent(this, userEventDto));
        return user;
    }

    public BoardRoles publish(EventType eventType, BoardRoles boardRoles) {
        var userBoardRoleEventDto = eventMapper.toEventDto(boardRoles, eventType);
        publish(new UserBoardRoleEvent(this, userBoardRoleEventDto));
        return boardRoles;
    }
}
