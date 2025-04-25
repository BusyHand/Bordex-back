package com.ugrasu.bordexback.dto.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.dto.slim.BoardSlimDto;
import com.ugrasu.bordexback.dto.slim.UserSlimDto;
import com.ugrasu.bordexback.dto.validation.OnCreate;
import com.ugrasu.bordexback.dto.validation.OnUpdate;
import com.ugrasu.bordexback.entity.enums.Priority;
import com.ugrasu.bordexback.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Полная информация о задаче")
public class TaskDto {

    @Null(
            message = "ID нельзя задавать вручную",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(description = "Идентификатор задачи", example = "10", accessMode = Schema.AccessMode.READ_ONLY)
    Long id;

    @NotNull(
            message = "Имя задачи не может быть null",
            groups = OnCreate.class
    )
    @Size(
            min = 1,
            max = 255,
            message = "Имя должно быть от 1 до 255 символов",
            groups = OnCreate.class
    )
    @NotBlank(
            message = "Имя задачи не может быть пустым",
            groups = OnCreate.class
    )
    @Schema(description = "Название задачи", example = "Сделать бэкенд")
    String name;

    @Size(
            max = 1000,
            message = "Описание не может превышать 1000 символов",
            groups = OnCreate.class
    )
    @Schema(description = "Описание задачи", example = "Нужно реализовать весь бэкенд проекта")
    String description;

    @Schema(description = "Статус задачи", example = "IN_PROGRESS")
    Status status;

    @Schema(description = "Приоритет задачи", example = "HIGH")
    Priority priority;

    @Schema(description = "Крайний срок выполнения", example = "2025-05-01T23:59:00")
    LocalDateTime deadline;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Valid
    @Schema(description = "Владелец задачи")
    UserSlimDto owner;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Valid
    @Schema(description = "Доска, к которой привязана задача")
    BoardSlimDto board;
}
