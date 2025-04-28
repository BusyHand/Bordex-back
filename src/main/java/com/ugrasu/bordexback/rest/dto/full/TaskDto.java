package com.ugrasu.bordexback.rest.dto.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.rest.dto.BaseDto;
import com.ugrasu.bordexback.rest.dto.slim.BoardSlimDto;
import com.ugrasu.bordexback.rest.dto.slim.UserSlimDto;
import com.ugrasu.bordexback.rest.dto.validation.OnCreate;
import com.ugrasu.bordexback.rest.dto.validation.OnUpdate;
import com.ugrasu.bordexback.rest.entity.enums.Priority;
import com.ugrasu.bordexback.rest.entity.enums.Status;
import com.ugrasu.bordexback.rest.entity.enums.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Полная информация о задаче")
public class TaskDto extends BaseDto {

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

    @Schema(description = "Тег задачи", example = "DEVELOPMENT")
    Tag tag;

    @Schema(description = "Цвет тега задачи", example = "#3498db")
    String tagColor;

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
    BoardSlimDto board;

    private Set<UserSlimDto> assignees = new LinkedHashSet<>();


}
