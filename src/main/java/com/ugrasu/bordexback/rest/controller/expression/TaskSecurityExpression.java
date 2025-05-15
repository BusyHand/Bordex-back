package com.ugrasu.bordexback.rest.controller.expression;

import com.ugrasu.bordexback.auth.security.authenfication.AuthenticatedUser;
import com.ugrasu.bordexback.rest.entity.BaseEntity;
import com.ugrasu.bordexback.rest.entity.BoardRoles;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.repository.BoardRolesRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component("tse")
@RequiredArgsConstructor
public class TaskSecurityExpression extends SecurityExpressionPrincipal {

    private final TaskRepository taskRepository;
    private final BoardRolesRepository boardRolesRepository;

    public boolean developerPatch(Long taskId, Task newTask) {
        Long userId = getPrincipal().getUserId();
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AccessDeniedException("Доступ запрещен"));

        if (task.getAssignees()
                .stream()
                .map(BaseEntity::getId)
                .noneMatch(userId::equals)) {
            return false;
        }
        return newTask.getDescription() == null && newTask.getName() == null && newTask.getPriority() == null
               && newTask.getTag() == null && newTask.getDeadline() == null;
    }

    public boolean hasBoardRoleByTaskId(Long taskId, String role) {
        if (taskId == null || role == null) {
            return false;
        }
        AuthenticatedUser authenticatedUser = getPrincipal();
        return hasBoardRoleByTaskId(authenticatedUser.getUserId(), taskId, role);
    }

    public boolean hasBoardRoleByTaskId(Long userId, Long taskId, String role) {
        if (userId == null || taskId == null || role == null) {
            return false;
        }
        Long boardId = taskRepository.findById(taskId)
                .orElseThrow(() -> new AccessDeniedException("Доступ к ресурсу запрещен"))
                .getBoard()
                .getId();
        BoardRoles boardRoles = boardRolesRepository.findByUser_IdAndBoard_Id(userId, boardId)
                .orElseThrow(() -> new AccessDeniedException("Доступ к ресурсу запрещен"));
        return boardRoles.getBoardRoles()
                .stream()
                .anyMatch(boardRole -> boardRole.name().equals(role));
    }

    public boolean isBoardOwnerByTaskId(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new AccessDeniedException("Доступ запрещен"));

        User owner = task.getBoard().getOwner();

        return owner.getId().equals(getPrincipal().getUserId());

    }

    public boolean canAccessTasks(Long boardId, String role) {
        if (boardId == null || role == null) {
            return false;
        }
        return boardRolesRepository.findByUser_IdAndBoard_Id(getPrincipal().getUserId(), boardId)
                .orElseThrow(() -> new AccessDeniedException("Доступ к ресурсу запрещен"))
                .getBoardRoles()
                .stream()
                .map(Enum::name)
                .anyMatch(role::equals);
    }

}
