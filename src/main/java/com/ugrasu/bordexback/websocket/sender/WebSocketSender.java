package com.ugrasu.bordexback.websocket.sender;

import com.ugrasu.bordexback.rest.dto.web.full.BoardDto;
import com.ugrasu.bordexback.rest.dto.web.full.TaskDto;
import com.ugrasu.bordexback.rest.dto.web.full.UserBoardRoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebSocketSender {

    //todo чтобы null не мог прийти в коллекции


    private final SimpMessagingTemplate messagingTemplate;

    public void sendUpdateBoard(BoardDto boardDto) {
        messagingTemplate.convertAndSend("/topic/board/" + boardDto.getId(), boardDto);
    }

    public void sendDeleteBoard(BoardDto boardDto) {
        messagingTemplate.convertAndSend("/topic/board/" + boardDto.getId() + "/delete", boardDto);
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

    public void sendUpdateBoardRole(UserBoardRoleDto userBoardRole) {
        Long boardId = userBoardRole.getBoard().getId();
        messagingTemplate.convertAndSend("/topic/board/" + boardId + "/roles", userBoardRole);
    }

    public void sendDeleteBoardRole(UserBoardRoleDto userBoardRole) {
        Long boardId = userBoardRole.getBoard().getId();
        messagingTemplate.convertAndSend("/topic/board/" + boardId + "/roles/delete", userBoardRole);
    }
}
