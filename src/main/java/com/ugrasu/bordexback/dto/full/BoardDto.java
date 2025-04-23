package com.ugrasu.bordexback.dto.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.dto.slim.TaskSlimDto;
import com.ugrasu.bordexback.dto.slim.UserBoardRoleSlimDto;
import com.ugrasu.bordexback.dto.slim.UserSlimDto;
import com.ugrasu.bordexback.dto.validation.OnCreate;
import com.ugrasu.bordexback.dto.validation.OnUpdate;
import com.ugrasu.bordexback.entity.enums.Scope;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Value;

import java.util.Set;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardDto {

    @Null(
            message = "Пользователь не имеет права задавать id",
            groups = {OnCreate.class, OnUpdate.class}
    )
    Long id;

    @NotNull(
            message = "Не может быть пустым",
            groups = OnCreate.class
    )
    @Size(
            min = 1,
            max = 255,
            message = "Длина имени должна быть от 1 до 255 символов",
            groups = OnCreate.class
    )
    @NotEmpty(
            message = "Имя не может быть пустым",
            groups = OnCreate.class
    )
    @NotBlank(
            message = "Имя не может быть пустым",
            groups = OnCreate.class
    )
    String name;

    @NotNull(
            message = "Не может быть пустым",
            groups = OnCreate.class
    )
    @Size(
            min = 1,
            max = 500,
            message = "Длина описания должна быть от 1 до 500 символов",
            groups = OnCreate.class
    )
    @NotEmpty(
            message = "Описание не может быть пустым",
            groups = OnCreate.class
    )
    @NotBlank(
            message = "Описание не может быть пустым",
            groups = OnCreate.class
    )
    String description;

    Scope scope;

    @Valid
    UserSlimDto owner;

    @Valid
    Set<TaskSlimDto> tasks;

    @Valid
    Set<UserSlimDto> boardUsers;

    @Valid
    Set<UserBoardRoleSlimDto> userBoardRoles;
}
