package com.ugrasu.bordexback.rest.facade;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.service.BoardService;
import com.ugrasu.bordexback.rest.service.TaskService;
import com.ugrasu.bordexback.rest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskFacadeManagement {

    private final TaskService taskService;
    private final UserService userService;
    private final BoardService boardService;

    public Task createTask(Long boardId, Long userId, Task task) {
        User owner = userService.findOne(userId);
        Board board = boardService.findOne(boardId);
        return taskService.save(board, owner, task);
    }

    @Transactional
    public Task assignUserToTask(Long taskId, Long userId) {
        User assignUser = userService.findOne(userId);
        return taskService.assignUser(taskId, assignUser);
    }

    @Transactional
    public Task unassignUserFromTask(Long taskId, Long unassignUserId) {
        User unassignUser = userService.findOne(unassignUserId);
        return taskService.unassignUser(taskId, unassignUser);
    }
}
