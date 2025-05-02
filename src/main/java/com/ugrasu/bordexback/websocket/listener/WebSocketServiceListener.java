package com.ugrasu.bordexback.websocket.listener;

import com.ugrasu.bordexback.rest.dto.event.BoardEventDto;
import com.ugrasu.bordexback.rest.dto.event.TaskEventDto;
import com.ugrasu.bordexback.rest.dto.event.UserEventDto;
import com.ugrasu.bordexback.rest.dto.web.full.BoardDto;
import com.ugrasu.bordexback.rest.dto.web.full.TaskDto;
import com.ugrasu.bordexback.rest.dto.web.full.UserDto;
import com.ugrasu.bordexback.rest.event.*;
import com.ugrasu.bordexback.websocket.mapper.WebSocketEventMapper;
import com.ugrasu.bordexback.websocket.sender.WebSocketSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.ugrasu.bordexback.rest.event.EventType.*;

@Async
@Component
@RequiredArgsConstructor
public class WebSocketServiceListener {

    private final WebSocketSender sender;
    private final WebSocketEventMapper eventMapper;

    @EventListener
    public void handleTaskEvent(TaskEvent taskEvent) {
        TaskEventDto taskEventDto = taskEvent.getTaskEventDto();
        TaskDto dto = eventMapper.toDto(taskEventDto);
        EventType eventType = taskEvent.getEventType();

        if (TASK_DELETED.equals(eventType)) {
            sender.sendDeleteTask(dto);
            return;
        }
        if (TASK_UNASSIGNED.equals(eventType)) {
            sender.sendTaskUnassignUser(dto, taskEventDto.getUnassignUserId());
            return;
        }
        sender.sendUpdateTask(dto);

    }

    @EventListener
    public void handleUserEvent(UserEvent userEvent) {
        UserEventDto userEventDto = userEvent.getUserEventDto();
        UserDto dto = eventMapper.toDto(userEventDto);
        EventType eventType = userEvent.getEventType();

        if (USER_DELETED.equals(eventType)) {
            //todo send user delete
            return;
        }
        //todo send user update
    }

    @EventListener
    public void handleBoardEvent(BoardEvent boardEvent) {
        BoardEventDto boardEventDto = boardEvent.getBoardEventDto();
        BoardDto dto = eventMapper.toDto(boardEventDto);
        EventType eventType = boardEvent.getEventType();

        if (BOARD_DELETED.equals(eventType)) {
            //TODO
            return;
        }
        if (BOARD_UNASSIGNED.equals(eventType)) {
            //TODO
            return;
        }
        if (BOARD_SCOPE_CHANGED.equals(eventType)) {
            //TODO
            return;
        }
        //todo
    }

    @EventListener
    public void handleUserBoardRoleEvent(UserBoardRoleEvent userBoardRoleEvent) {
        var userBoardRoleEventDto = userBoardRoleEvent.getUserBoardRoleEventDto();
        var dto = eventMapper.toDto(userBoardRoleEventDto);
        EventType eventType = userBoardRoleEvent.getEventType();

        if (BOARD_ROLE_DELETED.equals(eventType)) {
            //todo
            return;
        }
        //todo
    }
}
