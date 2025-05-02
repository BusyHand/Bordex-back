package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.mapper.impl.TaskMapper;
import com.ugrasu.bordexback.rest.publisher.EventPublisher;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import static com.ugrasu.bordexback.rest.event.EventType.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final BoardService boardService;
    private final EventPublisher eventPublisher;

    public Page<Task> findAll(Specification<Task> specification, Pageable pageable) {
        return taskRepository.findAll(specification, pageable);
    }

    public Task findOne(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task with id %s not found".formatted(id)));
    }

    public Task save(Long boardId, Task task, User owner) {
        Board board = boardService.findOne(boardId);
        task.setBoard(board);
        task.setOwner(owner);
        Task saved = taskRepository.save(task);
        return eventPublisher.publish(TASK_CREATED, saved);
    }

    public Task assignUser(Long taskId, Long userId) {
        if (!taskRepository.existsByTaskIdAndUserId(taskId, userId)) {
            taskRepository.assignUserToTask(taskId, userId);
        }
        return eventPublisher.publish(TASK_ASSIGNED, findOne(taskId));
    }

    public Task unassignUser(Long taskId, Long unassignUserId) {
        if (taskRepository.existsByTaskIdAndUserId(taskId, unassignUserId)) {
            taskRepository.unassignUserFromTask(taskId, unassignUserId);
        }
        return eventPublisher.publish(TASK_UNASSIGNED, findOne(taskId), unassignUserId);
    }

    public Task patch(Long taskId, Task newTask) {
        Task oldTask = findOne(taskId);
        Task updatedTask = taskMapper.partialUpdate(newTask, oldTask);
        Task savedUpdatedTask = taskRepository.save(updatedTask);
        return eventPublisher.publish(TASK_UPDATED, savedUpdatedTask);
    }

    public Task delete(Long id) {
        return eventPublisher.publish(TASK_DELETED,
                taskRepository.deleteTaskById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Task with id %s not found".formatted(id))));

    }
}
