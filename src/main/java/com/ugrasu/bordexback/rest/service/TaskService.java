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
import org.springframework.transaction.annotation.Transactional;

import static com.ugrasu.bordexback.rest.event.EventType.*;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final EventPublisher eventPublisher;

    public Page<Task> findAll(Specification<Task> specification, Pageable pageable) {
        return taskRepository.findAll(specification, pageable);
    }

    public Task findOne(Long id) {
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
    public Task patch(Long taskId, Task newTask) {
        Task oldTask = findOne(taskId);
        Task updatedTask = taskMapper.partialUpdate(newTask, oldTask);
        return eventPublisher.publish(TASK_UPDATED, updatedTask);
    }

    @Transactional
    public void delete(Long id) {
        Task taskToDelete = findOne(id);
        eventPublisher.publish(TASK_DELETED, taskToDelete);
        taskRepository.deleteTaskById(id);
    }
}
