package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.rest.controller.filter.TaskFilter;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.Status;
import com.ugrasu.bordexback.rest.mapper.impl.TaskMapper;
import com.ugrasu.bordexback.rest.publisher.EventPublisher;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

import static com.ugrasu.bordexback.rest.event.EventType.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final EventPublisher eventPublisher;


    @PreAuthorize(
            """
                    (@use.isTheSameUser(#taskFilter.assigneeIds())
                    or @bse.isOwner(#taskFilter.boardId())
                    or @tse.canAccessTasks(#taskFilter.boardId(), 'VIEWER'))"""
    )
    public Page<Task> findAll(@P("taskFilter") TaskFilter taskFilter, Pageable pageable) {
        return taskRepository.findAll(taskFilter.filter(), pageable);
    }

    @PreAuthorize("@tse.hasBoardRoleByTaskId(#id, 'VIEWER')")
    public Task findOne(@P("id") Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task with id %s not found".formatted(id)));
    }

    @Transactional
    public Task save(Board board, User owner, Task task) {
        if (task.getId() != null) {
            task.setId(null);
        }
        task.setBoard(board);
        task.setOwner(owner);
        taskRepository.save(task);
        return eventPublisher.publish(TASK_CREATED, task);
    }

    public Task assignUser(Long taskId, User assignedUser) {
        Task task = findOne(taskId);
        task.addAssignee(assignedUser);
        return eventPublisher.publish(TASK_ASSIGNED, task);
    }

    public Task unassignUser(Long taskId, User unassignedUser) {
        Task task = findOne(taskId);
        task.removeAssignee(unassignedUser);
        return eventPublisher.publish(TASK_UNASSIGNED, task, unassignedUser);
    }

    @Transactional
    @PreAuthorize(
            "@tse.hasBoardRoleByTaskId(#taskId, 'MANAGER')" +
            "or (@tse.hasBoardRoleByTaskId(#taskId, 'DEVELOPER') and @tse.developerPatch(#taskId, #newTask))"
    )
    public Task patch(@P("taskId") Long taskId, @P("newTask") Task newTask) {
        Task oldTask = findOne(taskId);
        Task updatedTask = taskMapper.partialUpdate(newTask, oldTask);
        return eventPublisher.publish(TASK_UPDATED, updatedTask);
    }

    @Transactional
    @PreAuthorize("@tse.hasBoardRoleByTaskId(#id, 'MANAGER')")
    public void delete(@P("id") Long id) {
        Task taskToDelete = findOne(id);
        eventPublisher.publish(TASK_DELETED, taskToDelete);
        taskRepository.deleteTaskById(id);
    }

    @Transactional
    public void delete(@P("ids") Set<Long> ids) {
        ids.forEach(id -> {
            Task taskToDelete = findOne(id);
            eventPublisher.publish(TASK_DELETED, taskToDelete);
            taskRepository.deleteTaskById(id);
        });
    }

    public Set<Task> findByStatusAndBoardId(Status status, Long boardId) {
        return taskRepository.findByStatusAndBoard_Id(status, boardId);
    }
}
