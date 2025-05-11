package com.ugrasu.bordexback.notification.listener;

import com.ugrasu.bordexback.notification.entity.Notification;
import com.ugrasu.bordexback.notification.mapper.NotificationMapper;
import com.ugrasu.bordexback.notification.service.NotificationService;
import com.ugrasu.bordexback.rest.dto.payload.BoardRolesPayload;
import com.ugrasu.bordexback.rest.dto.payload.UserPayload;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import com.ugrasu.bordexback.rest.event.BoardEvent;
import com.ugrasu.bordexback.rest.event.BoardRolesEvent;
import com.ugrasu.bordexback.rest.event.EventType;
import com.ugrasu.bordexback.rest.event.TaskEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static com.ugrasu.bordexback.rest.event.EventType.BOARD_CREATED;
import static com.ugrasu.bordexback.rest.event.EventType.BOARD_UPDATED;

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

        notification.setContent(type.formatContent(
                taskEvent.getTaskPayload().getName(),
                taskEvent.getTaskPayload().getBoard().getName()
        ));

        notification.setTitle(type.getTitle());
        notification.setLink("/board/" + taskEvent.getTaskPayload().getBoard().getId());
        notificationService.save(notification);
    }


    @EventListener
    public void handleBoardEvent(BoardEvent boardEvent) {
        EventType type = boardEvent.getBoardPayload().getEventType();
        if (BOARD_UPDATED.equals(type) || BOARD_CREATED.equals(type)) {
            return;
        }
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
        BoardRolesPayload boardRolePayload = boardRolesEvent.getBoardRolePayload();
        EventType type = boardRolePayload.getEventType();
        UserPayload payloadUser = boardRolePayload.getUser();
        Notification notification = notificationMapper.toEntity(boardRolePayload);

        notification.setTitle(type.getTitle());
        notification.setContent(type.formatContent(
                boardRolePayload.getBoardRoles().stream()
                        .map(BoardRole::name)
                        .reduce((first, second) -> first + ", " + second)
                        .orElse("Не указаны"),
                payloadUser.getUsername() != null ? payloadUser.getUsername() : payloadUser.getTelegramUsername(),
                boardRolePayload.getBoard().getName()
        ));

        notification.setLink("/boards/" + boardRolesEvent.getBoardRolePayload().getBoard().getId());
        notificationService.save(notification);
    }
}
