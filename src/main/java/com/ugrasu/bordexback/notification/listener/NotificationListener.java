package com.ugrasu.bordexback.notification.listener;

import com.ugrasu.bordexback.notification.entity.Notification;
import com.ugrasu.bordexback.notification.mapper.NotificationMapper;
import com.ugrasu.bordexback.notification.service.NotificationService;
import com.ugrasu.bordexback.rest.dto.payload.UserPayload;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import com.ugrasu.bordexback.rest.event.BoardRolesEvent;
import com.ugrasu.bordexback.rest.event.BoardEvent;
import com.ugrasu.bordexback.rest.event.EventType;
import com.ugrasu.bordexback.rest.event.TaskEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Set;

@Async
@Component
@RequiredArgsConstructor
public class NotificationListener {

    private final NotificationMapper notificationMapper;
    private final NotificationService notificationService;

    @EventListener
    public void handleTaskEvent(TaskEvent taskEvent) {
        EventType type = taskEvent.getTaskPayload().getEventType();
        Notification notification = notificationMapper.toEntity(taskEvent.getTaskPayload());
        Set<UserPayload> assignees = taskEvent.getTaskPayload().getAssignees();

        if (assignees != null && !assignees.isEmpty()) {
            String assigns = assignees.stream()
                    .map(UserPayload::getUsername)
                    .findFirst()
                    .orElse("Не назначен");
            notification.setContent(type.formatContent(
                    taskEvent.getTaskPayload().getName(),
                    taskEvent.getTaskPayload().getBoard().getName(),
                    assigns
            ));
        } else {
            notification.setContent(type.formatContent(
                    taskEvent.getTaskPayload().getName(),
                    taskEvent.getTaskPayload().getBoard().getName()));
        }

        notification.setTitle(type.getTitle());
        notification.setLink("/board/" + taskEvent.getTaskPayload().getBoard().getId());
        notificationService.save(notification);
    }

    @EventListener
    public void handleBoardEvent(BoardEvent boardEvent) {
        EventType type = boardEvent.getBoardPayload().getEventType();
        Notification notification = notificationMapper.toEntity(boardEvent.getBoardPayload());

        notification.setTitle(type.getTitle());
        notification.setContent(type.formatContent(
                boardEvent.getBoardPayload().getName()
        ));

        notification.setLink("/boards/" + boardEvent.getBoardPayload().getId());
        notificationService.save(notification);
    }

    @EventListener
    public void handleBoardRoles(BoardRolesEvent boardRolesEvent) {
        EventType type = boardRolesEvent.getBoardRolePayload().getEventType();
        Notification notification = notificationMapper.toEntity(boardRolesEvent.getBoardRolePayload());

        notification.setTitle(type.getTitle());
        notification.setContent(type.formatContent(
                boardRolesEvent.getBoardRolePayload().getBoardRoles().stream()
                        .map(BoardRole::name)
                        .reduce((first, second) -> first + ", " + second)
                        .orElse("Не указаны"),
                boardRolesEvent.getBoardRolePayload().getUser().getUsername(),
                boardRolesEvent.getBoardRolePayload().getBoard().getName()
        ));

        notification.setLink("/boards/" + boardRolesEvent.getBoardRolePayload().getBoard().getId());
        notificationService.save(notification);
    }
}
