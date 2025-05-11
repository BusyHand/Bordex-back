package com.ugrasu.bordexback.rest.dto.web.slim;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.rest.controller.validation.OnCreate;
import com.ugrasu.bordexback.rest.controller.validation.OnUpdate;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSlimDto {

    @Null(
            message = "ID задается системой",
            groups = {OnCreate.class, OnUpdate.class}
    )
    Long id;

    @Null(
            message = "Пользователь не имеет право задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
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

}
