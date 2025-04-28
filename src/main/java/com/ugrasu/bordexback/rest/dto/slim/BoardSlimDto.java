package com.ugrasu.bordexback.rest.dto.slim;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.rest.dto.validation.OnCreate;
import com.ugrasu.bordexback.rest.dto.validation.OnUpdate;
import com.ugrasu.bordexback.rest.entity.enums.Scope;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
            message = "Scope обязателен",
            groups = OnCreate.class
    )
    Scope scope;
}
