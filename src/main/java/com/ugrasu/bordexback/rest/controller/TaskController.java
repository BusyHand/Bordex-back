package com.ugrasu.bordexback.rest.controller;

import com.ugrasu.bordexback.rest.controller.filter.TaskFilter;
import com.ugrasu.bordexback.rest.dto.full.TaskDto;
import com.ugrasu.bordexback.rest.dto.validation.OnCreate;
import com.ugrasu.bordexback.rest.dto.validation.OnUpdate;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.mapper.impl.TaskMapper;
import com.ugrasu.bordexback.rest.service.TaskService;
import com.ugrasu.bordexback.rest.service.UserService;
import com.ugrasu.bordexback.websocket.BoardWebSocketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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

    //todo dev purpose
    private final UserService userService;
    private final BoardWebSocketService boardWebSocketService;

    @GetMapping
    @Operation(summary = "Получить список задач", description = "Возвращает постраничный список задач с фильтрацией")
    public PagedModel<TaskDto> getAll(@ParameterObject @ModelAttribute TaskFilter filter,
                                      @ParameterObject Pageable pageable) {
        var spec = filter.toSpecification();
        Page<Task> tasks = taskService.findAll(spec, pageable);
        Page<TaskDto> taskDtos = tasks.map(taskMapper::toDto);
        return new PagedModel<>(taskDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить задачу по ID", description = "Возвращает задачу по её идентификатору")
    public TaskDto findById(@PathVariable("id") Long id) {
        Task task = taskService.findOne(id);
        return taskMapper.toDto(task);
    }

    @PostMapping("/{board-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskDto save(@PathVariable("board-id") Long boardId,
                        @Validated(OnCreate.class) @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task saved = taskService.save(boardId, task, userService.findOne(1L));

        TaskDto dto = taskMapper.toDto(saved);
        boardWebSocketService.sendBoardTaskUpdate(dto);

        return dto;
    }

    @PatchMapping("/{task-id}/assign-user/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto assignUser(@PathVariable("task-id") Long taskId,
                              @PathVariable("user-id") Long userId) {
        Task task = taskService.assignUser(taskId, userId);
        TaskDto dto = taskMapper.toDto(task);
        boardWebSocketService.sendBoardTaskUpdate(dto);
        return dto;
    }

    @PatchMapping("/{task-id}/unassign-user/{user-id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto unassignUser(@PathVariable("task-id") Long taskId,
                                @PathVariable("user-id") Long userId) {
        Task task = taskService.unassignUser(taskId, userId);
        TaskDto dto = taskMapper.toDto(task);
        boardWebSocketService.sendBoardTaskUpdate(dto);
        return dto;
    }


    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TaskDto update(@PathVariable("id") Long id,
                          @Validated(OnUpdate.class) @RequestBody TaskDto taskDto) {
        Task task = taskMapper.toEntity(taskDto);
        Task patched = taskService.patch(id, task);

        TaskDto dto = taskMapper.toDto(patched);
        boardWebSocketService.sendBoardTaskUpdate(dto);

        return dto;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        Task taskToDelete = taskService.findOne(id);
        taskService.delete(taskToDelete);

        boardWebSocketService.sendBoardTaskDelete(taskMapper.toDto(taskToDelete));
    }
}
