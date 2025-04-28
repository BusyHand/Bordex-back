package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.mapper.impl.TaskMapper;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final BoardService boardService;
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final UserRepository userRepository;
    private final UserService userService;

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

    public Task assignUser(Long userId, Long taskId) {
        Task task = findOne(taskId);
        User assigneeUser = userService.findOne(userId);
        task.getAssignees().add(assigneeUser);
        assigneeUser.getAssigneesTask().add(task);
        return taskRepository.save(task);
    }

    public Task unassignUser(Long userId, Long taskId) {
        Task task = findOne(taskId);
        User assigneeUser = userService.findOne(userId);
        task.getAssignees().remove(assigneeUser);
        assigneeUser.getAssigneesTask().remove(task);
        return taskRepository.save(task);
    }

    public Task patch(Long taskId, Task newTask) {
        Task oldTask = findOne(taskId);
        Set<User> assignees = oldTask.getAssignees();
        Task updatedTask = taskMapper.partialUpdate(newTask, oldTask);
        updatedTask.getAssignees().addAll(assignees);
        assignees.forEach(user -> user.getAssigneesTask().add(updatedTask));
        return taskRepository.save(updatedTask);
    }

    public void delete(Long id) {
        taskRepository.deleteById(id);
    }

    public void delete(Task task) {
        taskRepository.delete(task);
    }

}
