package com.ugrasu.bordexback.controller;

import com.ugrasu.bordexback.dto.full.TaskDto;
import com.ugrasu.bordexback.entity.Task;
import com.ugrasu.bordexback.mapper.impl.TaskMapper;
import com.ugrasu.bordexback.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @GetMapping
    public PagedModel<TaskDto> findAll(Pageable pageable) {
        Page<Task> tasks = taskService.findAll(pageable);
        Page<TaskDto> taskDtos = tasks.map(taskMapper::toDto);
        return new PagedModel<>(taskDtos);
    }

    @GetMapping("/{id}")
    public TaskDto findById(@PathVariable Long id) {
        Task task = taskService.findOne(id);
        return taskMapper.toDto(task);
    }

    //TODO logged user
    @PostMapping("/{board-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto save(@PathVariable("board-id") Long boardId, @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        return taskMapper.toDto(taskService.save(boardId, task, null));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto update(@PathVariable Long id, @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task patched = taskService.patch(id, task);
        return taskMapper.toDto(patched);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
