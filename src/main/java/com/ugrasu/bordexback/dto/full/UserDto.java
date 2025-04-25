package com.ugrasu.bordexback.dto.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.dto.slim.BoardSlimDto;
import com.ugrasu.bordexback.dto.slim.TaskSlimDto;
import com.ugrasu.bordexback.dto.slim.UserBoardRoleSlimDto;
import com.ugrasu.bordexback.dto.validation.OnCreate;
import com.ugrasu.bordexback.dto.validation.OnUpdate;
import com.ugrasu.bordexback.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Полная информация о пользователе")
public class UserDto {

    @Null(
            message = "ID задается автоматически",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(description = "Идентификатор пользователя", example = "3", accessMode = Schema.AccessMode.READ_ONLY)
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
    @Schema(description = "Логин пользователя", example = "ivan123")
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
    @Schema(description = "Имя пользователя", example = "Иван")
    String firstName;

    @Size(
            max = 100,
            message = "Фамилия не может быть длиннее 100 символов",
            groups = OnCreate.class
    )
    @Schema(description = "Фамилия пользователя", example = "Иванов")
    String lastName;

    @NotNull(
            message = "Email обязателен",
            groups = OnCreate.class
    )
    @Email(
            message = "Некорректный формат email",
            groups = OnCreate.class
    )
    @Schema(description = "Email пользователя", example = "ivan@example.com")
    String email;

    @Schema(description = "Системные роли пользователя", example = "[\"ADMIN\"]")
    Set<Role> roles;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Valid
    @Schema(description = "Роли пользователя на досках")
    Set<UserBoardRoleSlimDto> userBoardRoles;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Valid
    @Schema(description = "Список задач пользователя")
    Set<TaskSlimDto> tasks;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Valid
    @Schema(description = "Список задач, созданных пользователем")
    Set<TaskSlimDto> owner_tasks;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Valid
    @Schema(description = "Список досок, созданных пользователем")
    Set<BoardSlimDto> boards;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Valid
    @Schema(description = "Список досок, к которым пользователь имеет доступ")
    Set<BoardSlimDto> userBoards;
}
