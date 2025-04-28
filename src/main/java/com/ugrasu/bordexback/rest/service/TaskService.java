package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.mapper.impl.TaskMapper;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final BoardService boardService;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

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
        return taskRepository.save(task);
    }

    public Task assignUser(Long taskId, Long userId) {
        if (!taskRepository.existsByTaskIdAndUserId(taskId, userId)) {
            taskRepository.assignUserToTask(taskId, userId);
        }
        return findOne(taskId);
    }


    public Task unassignUser(Long taskId, Long userId) {
        if (taskRepository.existsByTaskIdAndUserId(taskId, userId)) {
            taskRepository.unassignUserFromTask(taskId, userId);
        }
        return findOne(taskId);
    }

    public Task patch(Long taskId, Task newTask) {
        Task oldTask = findOne(taskId);
        Task updatedTask = taskMapper.partialUpdate(newTask, oldTask);
        return taskRepository.save(updatedTask);
    }


    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public void delete(Task task) {
        taskRepository.delete(task);
    }

}
