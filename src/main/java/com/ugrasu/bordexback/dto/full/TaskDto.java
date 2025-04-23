package com.ugrasu.bordexback.dto.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.dto.slim.BoardSlimDto;
import com.ugrasu.bordexback.dto.slim.UserSlimDto;
import com.ugrasu.bordexback.dto.validation.OnCreate;
import com.ugrasu.bordexback.dto.validation.OnUpdate;
import com.ugrasu.bordexback.entity.enums.Priority;
import com.ugrasu.bordexback.entity.enums.Status;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDto {

    @Null(
            message = "ID нельзя задавать вручную",
            groups = {OnCreate.class, OnUpdate.class}
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
    String name;

    @Size(
            max = 1000,
            message = "Описание не может превышать 1000 символов",
            groups = OnCreate.class
    )
    String description;

    Status status;

    Priority priority;

    LocalDateTime deadline;

    @Valid
    UserSlimDto owner;

    @NotNull(
            message = "Задача должна быть привязана к доске",
            groups = OnCreate.class
    )
    @Valid
    BoardSlimDto board;
}
