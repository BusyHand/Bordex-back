package com.ugrasu.bordexback.dto.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.dto.slim.BoardSlimDto;
import com.ugrasu.bordexback.dto.slim.TaskSlimDto;
import com.ugrasu.bordexback.dto.slim.UserBoardRoleSlimDto;
import com.ugrasu.bordexback.dto.validation.OnCreate;
import com.ugrasu.bordexback.dto.validation.OnUpdate;
import com.ugrasu.bordexback.entity.enums.Role;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Value;

import java.util.Set;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    @Null(
            message = "ID задается автоматически",
            groups = {OnCreate.class, OnUpdate.class}
    )
    Long id;

    @NotNull(
            message = "Имя пользователя обязательно",
            groups = OnCreate.class
    )
    @Size(
            min = 3,
            max = 100,
            message = "Имя пользователя должно быть от 3 до 100 символов",
            groups = OnCreate.class
    )
    @NotBlank(
            message = "Имя пользователя не может быть пустым",
            groups = OnCreate.class
    )
    String username;

    @NotNull(
            message = "Имя обязательно",
            groups = OnCreate.class
    )
    @Size(
            min = 1,
            max = 100,
            message = "Имя должно быть от 1 до 100 символов",
            groups = OnCreate.class
    )
    @NotBlank(
            message = "Имя не может быть пустым",
            groups = OnCreate.class
    )
    String firstName;

    @Size(
            max = 100,
            message = "Фамилия не может быть длиннее 100 символов",
            groups = OnCreate.class
    )
    String lastName;

    @NotNull(
            message = "Email обязателен",
            groups = OnCreate.class
    )
    @Email(
            message = "Некорректный формат email",
            groups = OnCreate.class
    )
    String email;

    Set<Role> roles;

    @Valid
    Set<UserBoardRoleSlimDto> userBoardRoles;

    @Valid
    Set<TaskSlimDto> tasks;

    @Valid
    Set<TaskSlimDto> owner_tasks;

    @Valid
    Set<BoardSlimDto> boards;

    @Valid
    Set<BoardSlimDto> userBoards;
}
