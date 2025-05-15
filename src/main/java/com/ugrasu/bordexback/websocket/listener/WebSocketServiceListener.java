package com.ugrasu.bordexback.websocket.listener;

import com.ugrasu.bordexback.notification.dto.event.ConsumerPayload;
import com.ugrasu.bordexback.notification.dto.event.NotificationPayload;
import com.ugrasu.bordexback.notification.dto.web.NotificationDto;
import com.ugrasu.bordexback.notification.event.NotificationEvent;
import com.ugrasu.bordexback.rest.dto.payload.BoardPayload;
import com.ugrasu.bordexback.rest.dto.payload.TaskPayload;
import com.ugrasu.bordexback.rest.dto.payload.UserPayload;
import com.ugrasu.bordexback.rest.dto.web.full.BoardDto;
import com.ugrasu.bordexback.rest.dto.web.full.TaskDto;
import com.ugrasu.bordexback.rest.dto.web.slim.UserSlimDto;
import com.ugrasu.bordexback.rest.event.BoardEvent;
import com.ugrasu.bordexback.rest.event.BoardRolesEvent;
import com.ugrasu.bordexback.rest.event.EventType;
import com.ugrasu.bordexback.rest.event.TaskEvent;
import com.ugrasu.bordexback.websocket.mapper.WebSocketEventMapper;
import com.ugrasu.bordexback.websocket.sender.WebSocketSender;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

import static com.ugrasu.bordexback.rest.event.EventType.*;

@Async
@Component
@RequiredArgsConstructor
public class WebSocketServiceListener {

    private final WebSocketSender sender;
    private final WebSocketEventMapper eventMapper;

    @EventListener
    public void handleTaskEvent(TaskEvent taskEvent) {
        TaskPayload taskPayload = taskEvent.getTaskPayload();
        TaskDto dto = eventMapper.toDto(taskPayload);
        EventType eventType = taskPayload.getEventType();

        if (TASK_DELETED.equals(eventType)) {
            sender.sendDeleteTask(dto);
            return;
        }
        if (TASK_UNASSIGNED.equals(eventType)) {
            //todo bug
            UserPayload unassignUser = taskPayload.getUnassignUser();
            Set<UserSlimDto> userSlimDtoStream = dto.getAssignees()
                    .stream()
                    .filter(assing -> !assing.getId().equals(unassignUser.getId()))
                    .collect(Collectors.toSet());
            dto.setAssignees(userSlimDtoStream);
            sender.sendTaskUnassignUser(dto, unassignUser.getId());
            return;
        }
        sender.sendUpdateTask(dto);
    }

    @EventListener
    public void handleBoardEvent(BoardEvent boardEvent) {
        BoardPayload boardPayload = boardEvent.getBoardPayload();
        BoardDto dto = eventMapper.toDto(boardPayload);
        EventType eventType = boardPayload.getEventType();
        Set<Long> boardMembersIds = boardPayload.getBoardMembers()
                .stream()
                .map(UserPayload::getId)
                .collect(Collectors.toSet());

        if (BOARD_DELETED.equals(eventType)) {
            sender.sendDeleteBoard(dto, boardMembersIds);
            return;
        }
        if (BOARD_UNASSIGNED.equals(eventType)) {
            sender.sendUnassignedUser(dto, boardMembersIds, boardPayload.getUnassignUser().getId());
            return;
        }
        sender.sendUpdateBoard(dto, boardMembersIds);
    }

    @EventListener
    public void handleUserBoardRoleEvent(BoardRolesEvent boardRolesEvent) {
        var userBoardRoleEventDto = boardRolesEvent.getBoardRolePayload();
        var dto = eventMapper.toDto(userBoardRoleEventDto);
        sender.sendUpdateBoardRole(dto);
    }

    @EventListener
    public void handleTaskEvent(NotificationEvent notificationEvent) {
        NotificationPayload notificationPayload = notificationEvent.getNotificationPayload();
        NotificationDto dto = eventMapper.toDto(notificationPayload);
        Set<Long> consumersUsersId = notificationPayload.getConsumers()
                .stream()
                .filter(ConsumerPayload::getAllowOnSiteNotifications)
                .map(ConsumerPayload::getUserId)
                .collect(Collectors.toSet());
        sender.sendNotification(dto, consumersUsersId);
    }
}
