package com.ugrasu.bordexback.rest.dto.web.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.rest.controller.validation.OnCreate;
import com.ugrasu.bordexback.rest.controller.validation.OnUpdate;
import com.ugrasu.bordexback.rest.dto.BaseDto;
import com.ugrasu.bordexback.rest.dto.web.slim.UserSlimDto;
import com.ugrasu.bordexback.rest.entity.enums.Scope;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Полная информация о доске")
public class BoardDto extends BaseDto {

    @Null(
            message = "Пользователь не имеет права задавать id",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(
            description = "Идентификатор доски",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY
    )
    Long id;

    @NotNull(
            message = "Не может быть пустым",
            groups = OnCreate.class
    )
    @Size(
            min = 1,
            max = 30,
            message = "Длина имени должна быть от 1 до 30 символов",
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
    @Schema(
            description = "Название доски",
            example = "Проект A"
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
    @Schema(
            description = "Описание доски",
            example = "Описание проекта A"
    )
    String description;

    @Schema(
            description = "Уровень доступа к доске",
            example = "PRIVATE"
    )
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
    Long tasksCount;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    Long membersCount;

}
