package com.ugrasu.bordexback.rest.dto.web.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.rest.dto.BaseDto;
import com.ugrasu.bordexback.rest.dto.web.slim.BoardSlimDto;
import com.ugrasu.bordexback.rest.dto.web.slim.UserSlimDto;
import com.ugrasu.bordexback.rest.controller.validation.OnCreate;
import com.ugrasu.bordexback.rest.controller.validation.OnUpdate;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Роль пользователя на доске")
public class BoardRolesDto extends BaseDto {

    @Null(
            message = "ID нельзя задавать вручную",
            groups = {OnCreate.class, OnUpdate.class}
    )
    @Schema(
            description = "Идентификатор",
            example = "5",
            accessMode = Schema.AccessMode.READ_ONLY
    )
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
    @Schema(
            description = "Список ролей",
            example = "[\"READER\", \"WRITER\"]"
    )
    Set<BoardRole> boardRoles;
}
