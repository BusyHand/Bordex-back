package com.ugrasu.bordexback.rest.controller;

import com.ugrasu.bordexback.auth.security.AuthenficatedUser;
import com.ugrasu.bordexback.rest.controller.filter.TaskFilter;
import com.ugrasu.bordexback.rest.controller.validation.OnCreate;
import com.ugrasu.bordexback.rest.controller.validation.OnUpdate;
import com.ugrasu.bordexback.rest.dto.web.full.TaskDto;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.mapper.impl.TaskMapper;
import com.ugrasu.bordexback.rest.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Задачи",
        description = "Управление задачами"
)
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;
    private final TaskMapper taskMapper;

    @Operation(
            summary = "Получить список задач",
            description = "Возвращает постраничный список задач с возможностью фильтрации по параметрам"
    )
    @GetMapping
    public PagedModel<TaskDto> findAll(@ParameterObject @ModelAttribute TaskFilter filter,
                                       @ParameterObject Pageable pageable) {
        var spec = filter.toSpecification();
        Page<Task> tasks = taskService.findAll(spec, pageable);
        Page<TaskDto> taskDtos = tasks.map(taskMapper::toDto);
        return new PagedModel<>(taskDtos);
    }

    @Operation(
            summary = "Получить задачу по ID",
            description = "Возвращает полную информацию о задаче по её идентификатору"
    )
    @GetMapping("/{id}")
    public TaskDto findById(@PathVariable("id") Long id) {
        Task task = taskService.findOne(id);
        return taskMapper.toDto(task);
    }

    @Operation(
            summary = "Создать новую задачу",
            description = "Создаёт новую задачу в указанной доске"
    )
    @PostMapping("/{board-id}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("isAuthenticated()")
    public TaskDto create(@AuthenticationPrincipal AuthenficatedUser authenficatedUser,
                          @PathVariable("board-id") Long boardId,
                          @Validated(OnCreate.class) @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task saved = taskService.save(boardId, task, authenficatedUser.getUserId());
        return taskMapper.toDto(saved);
    }

    @Operation(
            summary = "Обновить задачу",
            description = "Вносит частичные изменения в существующую задачу по её идентификатору"
    )
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto update(@PathVariable("id") Long id,
                          @Validated(OnUpdate.class) @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task patched = taskService.patch(id, task);
        return taskMapper.toDto(patched);
    }

    @Operation(
            summary = "Удалить задачу",
            description = "Удаляет задачу по её идентификатору"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        taskService.delete(id);
    }

    @Operation(
            summary = "Назначить пользователя на задачу",
            description = "Назначает указанного пользователя на задачу"
    )
    @PatchMapping("/{task-id}/assign-user/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto assignUser(@PathVariable("task-id") Long taskId,
                              @PathVariable("user-id") Long userId) {
        Task task = taskService.assignUser(taskId, userId);
        return taskMapper.toDto(task);
    }

    @Operation(
            summary = "Отвязать пользователя от задачи",
            description = "Удаляет назначение пользователя с задачи"
    )
    @PatchMapping("/{task-id}/unassign-user/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto unassignUser(@PathVariable("task-id") Long taskId,
                                @PathVariable("user-id") Long userId) {
        Task task = taskService.unassignUser(taskId, userId);
        return taskMapper.toDto(task);
    }
}
