package com.ugrasu.bordexback.dto.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.dto.slim.BoardSlimDto;
import com.ugrasu.bordexback.dto.slim.UserSlimDto;
import com.ugrasu.bordexback.dto.validation.OnCreate;
import com.ugrasu.bordexback.dto.validation.OnUpdate;
import com.ugrasu.bordexback.entity.enums.BoardRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.util.Set;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBoardRoleDto {

    @Null(
            message = "ID нельзя задавать вручную",
            groups = {OnCreate.class, OnUpdate.class}
    )
    Long id;

    @Valid
    @NotNull(
            message = "Пользователь обязателен",
            groups = OnCreate.class
    )
    UserSlimDto user;

    @Valid
    @NotNull(
            message = "Доска обязательна",
            groups = OnCreate.class
    )
    BoardSlimDto board;

    @NotNull(
            message = "Роли обязательны",
            groups = OnCreate.class
    )
    @Size(
            min = 1,
            message = "Должна быть хотя бы одна роль",
            groups = OnCreate.class
    )
    Set<BoardRole> boardRoles;
}
