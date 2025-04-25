package com.ugrasu.bordexback.dto.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.dto.slim.BoardSlimDto;
import com.ugrasu.bordexback.dto.slim.UserSlimDto;
import com.ugrasu.bordexback.dto.validation.OnCreate;
import com.ugrasu.bordexback.dto.validation.OnUpdate;
import com.ugrasu.bordexback.entity.enums.BoardRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.util.Set;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Роль пользователя на доске")
public class UserBoardRoleDto {

    @Null(
            message = "ID нельзя задавать вручную",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(description = "Идентификатор", example = "5", accessMode = Schema.AccessMode.READ_ONLY)
    Long id;

    @Valid
    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(description = "Пользователь")
    UserSlimDto user;

    @Valid
    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(description = "Доска")
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
    @Schema(description = "Список ролей", example = "[\"READER\", \"WRITER\"]")
    Set<BoardRole> boardRoles;
}
