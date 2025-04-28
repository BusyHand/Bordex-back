package com.ugrasu.bordexback.websocket;

import com.ugrasu.bordexback.rest.dto.full.BoardDto;
import com.ugrasu.bordexback.rest.dto.full.TaskDto;
import com.ugrasu.bordexback.rest.dto.full.UserBoardRoleDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardWebSocketService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendBoardRoleUpdate(UserBoardRoleDto userBoardRole) {
        Long boardId = userBoardRole.getBoard().getId();
        messagingTemplate.convertAndSend("/topic/board/" + boardId + "/roles", userBoardRole);
    }

    public void sendBoardRoleDelete(UserBoardRoleDto userBoardRole) {
        Long boardId = userBoardRole.getBoard().getId();
        messagingTemplate.convertAndSend("/topic/board/" + boardId + "/roles/delete", userBoardRole);
    }

    public void sendBoardTaskUpdate(TaskDto task) {
        Long boardId = task.getBoard().getId();
        messagingTemplate.convertAndSend("/topic/board/" + boardId + "/tasks", task);
    }

    public void sendBoardTaskDelete(TaskDto task) {
        Long boardId = task.getBoard().getId();
        messagingTemplate.convertAndSend("/topic/board/" + boardId + "/tasks/delete", task);
    }

    public void sendUserBoardsUpdate(BoardDto boardDto) {
        messagingTemplate.convertAndSendToUser("user 1", "/topic/user/boards/", boardDto);
    }

    public void sendUserBoardsDelete(BoardDto boardDto) {
        messagingTemplate.convertAndSendToUser("user 1", "/topic/user/boards/", boardDto);
    }
}
