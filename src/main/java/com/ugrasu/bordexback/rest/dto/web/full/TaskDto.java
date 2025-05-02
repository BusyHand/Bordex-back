package com.ugrasu.bordexback.rest.dto.web.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.rest.controller.validation.OnCreate;
import com.ugrasu.bordexback.rest.controller.validation.OnUpdate;
import com.ugrasu.bordexback.rest.dto.BaseDto;
import com.ugrasu.bordexback.rest.dto.web.slim.BoardSlimDto;
import com.ugrasu.bordexback.rest.dto.web.slim.UserSlimDto;
import com.ugrasu.bordexback.rest.entity.enums.Priority;
import com.ugrasu.bordexback.rest.entity.enums.Status;
import com.ugrasu.bordexback.rest.entity.enums.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;


@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Полная информация о задаче")
public class TaskDto extends BaseDto {

    @Null(
            message = "ID нельзя задавать вручную",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(
            description = "Идентификатор задачи",
            example = "10",
            accessMode = Schema.AccessMode.READ_ONLY
    )
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
    @Schema(
            description = "Название задачи",
            example = "Сделать бэкенд"
    )
    String name;

    @Size(
            max = 1000,
            message = "Описание не может превышать 1000 символов",
            groups = OnCreate.class
    )
    @Schema(
            description = "Описание задачи",
            example = "Нужно реализовать весь бэкенд проекта"
    )
    String description;

    @Schema(
            description = "Статус задачи",
            example = "IN_PROGRESS"
    )
    Status status;

    @Schema(
            description = "Приоритет задачи",
            example = "HIGH"
    )
    Priority priority;

    @Schema(
            description = "Тег задачи",
            example = "DEVELOPMENT"
    )
    Tag tag;

    @Schema(
            description = "Цвет тега задачи",
            example = "#3498db"
    )
    String tagColor;

    @Min(
            value = 0,
            message = "Не может быть меньше нуля"
    )
    @Max(
            value = 100,
            message = "Не может превышать 100"
    )
    @NotNull(
            message = "Должен быть параметр",
            groups = OnCreate.class
    )
    Integer progress;

    @Schema(
            description = "Крайний срок выполнения",
            example = "2025-05-01T23:59:00"
    )
    LocalDateTime deadline;

    @Min(1)
    @Schema(
            description = "Номер строчки в колонке",
            example = "2025-05-01T23:59:00"
    )
    Long columRowNumber;

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

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    Set<UserSlimDto> assignees;


}
