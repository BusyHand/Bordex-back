package com.ugrasu.bordexback.websocket.sender;

import com.ugrasu.bordexback.notification.dto.web.NotificationDto;
import com.ugrasu.bordexback.rest.dto.web.full.BoardDto;
import com.ugrasu.bordexback.rest.dto.web.full.BoardRolesDto;
import com.ugrasu.bordexback.rest.dto.web.full.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class WebSocketSender {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendNotification(NotificationDto notificationDto, Set<Long> consumersUsersId) {
        consumersUsersId.forEach(userId ->
                messagingTemplate.convertAndSend("/topic/notification/user/" + userId, notificationDto));
    }

    public void sendUpdateBoard(BoardDto boardDto, Set<Long> boardMembersIds) {
        boardMembersIds.forEach(memberId -> messagingTemplate.convertAndSend("/topic/user/" + memberId + "/board", boardDto));
    }

    public void sendDeleteBoard(BoardDto boardDto, Set<Long> boardMembersIds) {
        boardMembersIds.forEach(memberId -> messagingTemplate.convertAndSend("/topic/user/" + memberId + "/board/delete", boardDto));
    }

    public void sendUnassignedUser(BoardDto boardDto, Set<Long> boardMembersIds, Long unassignUserId) {
        boardMembersIds.forEach(memberId -> messagingTemplate.convertAndSend("/topic/user/" + memberId + "/board", boardDto));
        messagingTemplate.convertAndSend("/topic/user/" + unassignUserId + "/board", boardDto);

    }

    public void sendUpdateTask(TaskDto task) {
        Long boardId = task.getBoard().getId();
        messagingTemplate.convertAndSend("/topic/board/" + boardId + "/tasks", task);
        task.getAssignees()
                .forEach(assignee -> messagingTemplate.convertAndSend("/topic/users/" + assignee.getId() + "/tasks", task));
    }

    public void sendTaskUnassignUser(TaskDto task, Long unassignUserId) {
        Long boardId = task.getBoard().getId();
        messagingTemplate.convertAndSend("/topic/board/" + boardId + "/tasks", task);
        task.getAssignees()
                .forEach(assignee -> messagingTemplate.convertAndSend("/topic/users/" + assignee.getId() + "/tasks", task));
        messagingTemplate.convertAndSend("/topic/users/" + unassignUserId + "/tasks", task);
    }

    public void sendDeleteTask(TaskDto task) {
        Long boardId = task.getBoard().getId();
        messagingTemplate.convertAndSend("/topic/board/" + boardId + "/tasks/delete", task);
        task.getAssignees()
                .forEach(assignee -> messagingTemplate.convertAndSend("/topic/users/" + assignee.getId() + "/tasks/delete", task));
    }

    public void sendUpdateBoardRole(BoardRolesDto userBoardRole) {
        Long boardId = userBoardRole.getBoard().getId();
        messagingTemplate.convertAndSend("/topic/board/" + boardId + "/roles", userBoardRole);
    }
}