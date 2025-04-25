package com.ugrasu.bordexback.dto.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.dto.slim.TaskSlimDto;
import com.ugrasu.bordexback.dto.slim.UserBoardRoleSlimDto;
import com.ugrasu.bordexback.dto.slim.UserSlimDto;
import com.ugrasu.bordexback.dto.validation.OnCreate;
import com.ugrasu.bordexback.dto.validation.OnUpdate;
import com.ugrasu.bordexback.entity.enums.Scope;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Полная информация о доске")
public class BoardDto {

    @Null(
            message = "Пользователь не имеет права задавать id",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(description = "Идентификатор доски", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
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
    @Schema(description = "Название доски", example = "Проект A")
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
    @Schema(description = "Описание доски", example = "Описание проекта A")
    String description;

    @Schema(description = "Уровень доступа к доске", example = "PRIVATE")
    Scope scope;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(description = "Владелец доски")
    UserSlimDto owner;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(description = "Список задач на доске")
    Set<TaskSlimDto> tasks;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(description = "Пользователи, привязанные к доске")
    Set<UserSlimDto> boardUsers;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(description = "Роли пользователей на доске")
    Set<UserBoardRoleSlimDto> userBoardRoles;
}
