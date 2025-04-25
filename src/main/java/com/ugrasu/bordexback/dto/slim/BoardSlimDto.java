package com.ugrasu.bordexback.dto.slim;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.dto.validation.OnCreate;
import com.ugrasu.bordexback.dto.validation.OnUpdate;
import com.ugrasu.bordexback.entity.enums.Scope;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardSlimDto {

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
            max = 100,
            message = "Название должно быть от 1 до 100 символов",
            groups = OnCreate.class
    )
    String name;

    @NotNull(
            message = "Описание обязательно",
            groups = OnCreate.class
    )
    @Size(
            min = 1,
            max = 500,
            message = "Описание должно быть от 1 до 500 символов",
            groups = OnCreate.class
    )
    String description;

    @NotNull(
            message = "Scope обязателен",
            groups = OnCreate.class
    )
    Scope scope;
}
