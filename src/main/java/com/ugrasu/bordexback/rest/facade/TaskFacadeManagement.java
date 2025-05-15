package com.ugrasu.bordexback.rest.facade;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.service.BoardService;
import com.ugrasu.bordexback.rest.service.TaskService;
import com.ugrasu.bordexback.rest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskFacadeManagement {

    private final TaskService taskService;
    private final UserService userService;
    private final BoardService boardService;

    @Transactional
    @PreAuthorize("@bse.isOwner(#boardId) or @brse.hasBoardRole(#boardId, 'MANAGER')")
    public Task createTask(@P("boardId") Long boardId, @P("userId") Long userId, Task task) {
        User owner = userService.findOne(userId);
        Board board = boardService.findOne(boardId);
        return taskService.save(board, owner, task);
    }

    @Transactional
    @PreAuthorize(
            """      
                    @tse.isBoardOwnerByTaskId(#taskId) or
                    (@tse.hasBoardRoleByTaskId(#taskId, 'MANAGER')
                    and @tse.hasBoardRoleByTaskId(#userId, #taskId, 'DEVELOPER'))"""
    )
    public Task assignUserToTask(@P("taskId") Long taskId, @P("userId") Long userId) {
        User assignUser = userService.findOne(userId);
        return taskService.assignUser(taskId, assignUser);
    }

    @Transactional
    @PreAuthorize(
            """
                    @tse.isBoardOwnerByTaskId(#taskId) or
                    @tse.hasBoardRoleByTaskId(#taskId, 'MANAGER')"""
    )
    public Task unassignUserFromTask(@P("taskId") Long taskId, @P("unassignUserId") Long unassignUserId) {
        User unassignUser = userService.findOne(unassignUserId);
        return taskService.unassignUser(taskId, unassignUser);
    }
}
