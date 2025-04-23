package com.ugrasu.bordexback.dto.slim;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.dto.validation.OnCreate;
import com.ugrasu.bordexback.dto.validation.OnUpdate;
import com.ugrasu.bordexback.entity.enums.Priority;
import com.ugrasu.bordexback.entity.enums.Status;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskSlimDto {

    @Null(
            message = "ID нельзя задавать вручную",
            groups = {OnCreate.class, OnUpdate.class}
    )
    Long id;

    @NotNull(
            message = "Название обязательно",
            groups = OnCreate.class
    )
    @Size(
            min = 1,
            max = 200,
            message = "Название должно быть от 1 до 200 символов",
            groups = OnCreate.class
    )
    String name;

    @NotNull(
            message = "Описание обязательно",
            groups = OnCreate.class
    )
    @Size(
            min = 1,
            max = 1000,
            message = "Описание должно быть от 1 до 1000 символов",
            groups = OnCreate.class
    )
    String description;

    @NotNull(
            message = "Статус обязателен",
            groups = OnCreate.class
    )
    Status status;

    @NotNull(
            message = "Приоритет обязателен",
            groups = OnCreate.class
    )
    Priority priority;

    @FutureOrPresent(
            message = "Дедлайн не может быть в прошлом",
            groups = OnCreate.class
    )
    LocalDateTime deadline;
}
