package com.ugrasu.bordexback.controller;

import com.ugrasu.bordexback.dto.full.TaskDto;
import com.ugrasu.bordexback.dto.validation.OnCreate;
import com.ugrasu.bordexback.dto.validation.OnUpdate;
import com.ugrasu.bordexback.entity.Task;
import com.ugrasu.bordexback.mapper.impl.TaskMapper;
import com.ugrasu.bordexback.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Задачи", description = "Управление задачами")
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Operation(summary = "Получить список задач", description = "Возвращает постраничный список задач")
    @GetMapping
    public PagedModel<TaskDto> findAll(Pageable pageable) {
        Page<Task> tasks = taskService.findAll(pageable);
        Page<TaskDto> taskDtos = tasks.map(taskMapper::toDto);
        return new PagedModel<>(taskDtos);
    }

    @Operation(summary = "Получить задачу по ID", description = "Возвращает задачу по её идентификатору")
    @GetMapping("/{id}")
    public TaskDto findById(@PathVariable Long id) {
        Task task = taskService.findOne(id);
        return taskMapper.toDto(task);
    }

    @Operation(summary = "Создать новую задачу", description = "Создаёт новую задачу на указанной доске")
    @PostMapping("/{board-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto save(
            @PathVariable("board-id") Long boardId,
            @Validated(OnCreate.class) @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        return taskMapper.toDto(taskService.save(boardId, task, null));
    }

    @Operation(summary = "Обновить задачу", description = "Частично обновляет данные задачи по ID")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto update(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task patched = taskService.patch(id, task);
        return taskMapper.toDto(patched);
    }

    @Operation(summary = "Удалить задачу", description = "Удаляет задачу по её идентификатору")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        taskService.delete(id);
    }
}
