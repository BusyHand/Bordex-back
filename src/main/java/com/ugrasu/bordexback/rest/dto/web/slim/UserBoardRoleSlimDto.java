package com.ugrasu.bordexback.rest.dto.web.slim;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.rest.controller.validation.OnCreate;
import com.ugrasu.bordexback.rest.controller.validation.OnUpdate;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBoardRoleSlimDto {

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
