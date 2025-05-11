package com.ugrasu.bordexback.rest.dto.web.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.rest.controller.validation.OnCreate;
import com.ugrasu.bordexback.rest.controller.validation.OnUpdate;
import com.ugrasu.bordexback.rest.dto.BaseDto;
import com.ugrasu.bordexback.rest.dto.web.slim.BoardRolesSlimDto;
import com.ugrasu.bordexback.rest.entity.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Полная информация о пользователе")
public class UserDto extends BaseDto {

    @Null(
            message = "ID задается автоматически",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(
            description = "Идентификатор пользователя",
            example = "3",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    Long id;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(
            description = "Имя пользователя в телеграме",
            example = "bordex_bot",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    String telegramUsername;

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
    @Schema(
            description = "Логин пользователя",
            example = "ivan123"
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
    @Schema(
            description = "Имя пользователя",
            example = "Иван"
    )
    String firstName;

    @Size(
            max = 100,
            message = "Фамилия не может быть длиннее 100 символов",
            groups = OnCreate.class
    )
    @Schema(
            description = "Фамилия пользователя",
            example = "Иванов"
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
    @Schema(
            description = "Email пользователя",
            example = "ivan@example.com"
    )
    String email;

    @Schema(
            description = "Системные роли пользователя",
            example = "[\"ADMIN\"]"
    )
    Set<Role> roles;

    @Null(
            message = "ID задается автоматически",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(
            description = "Все роли пользователя на досках в которых он состоит",
            example = "[\"ADMIN\"]"
    )
    Set<BoardRolesSlimDto> boardsRoles;

}
