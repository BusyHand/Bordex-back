package com.ugrasu.bordexback.auth.dto;

import com.ugrasu.bordexback.auth.dto.validation.OnLogin;
import com.ugrasu.bordexback.auth.dto.validation.OnLoginTelegram;
import com.ugrasu.bordexback.auth.dto.validation.OnRegister;
import com.ugrasu.bordexback.auth.dto.validation.OnTelegramPostRegister;
import com.ugrasu.bordexback.rest.controller.validation.OnUpdate;
import com.ugrasu.bordexback.rest.entity.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthDto {

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = OnLoginTelegram.class
    )
    @NotBlank(
            message = "Имя пользователя не может быть пустым",
            groups = {OnLogin.class, OnRegister.class, OnTelegramPostRegister.class}
    )
    @Size(
            min = 3,
            max = 50,
            message = "Имя пользователя должно быть от 3 до 50 символов",
            groups = {OnLogin.class, OnRegister.class, OnTelegramPostRegister.class}
    )
    String username;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = OnLoginTelegram.class
    )
    @NotBlank(
            message = "Имя пользователя не может быть пустым",
            groups = {OnRegister.class}
    )
    @Size(
            min = 3,
            max = 50,
            message = "Имя пользователя должно быть от 3 до 50 символов",
            groups = {OnRegister.class}
    )
    String firstName;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = OnLoginTelegram.class
    )
    @NotBlank(
            message = "Фамилия пользователя не может быть пустым",
            groups = {OnRegister.class}
    )
    @Size(
            min = 3,
            max = 50,
            message = "Имя пользователя должно быть от 3 до 50 символов",
            groups = {OnRegister.class}
    )
    String lastName;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = OnLoginTelegram.class
    )
    @NotBlank(
            message = "Пароль не может быть пустым",
            groups = {OnLogin.class, OnRegister.class, OnTelegramPostRegister.class}
    )
    @Size(
            min = 6,
            max = 100,
            message = "Пароль должен быть от 6 до 100 символов",
            groups = {OnLogin.class, OnRegister.class, OnTelegramPostRegister.class}
    )
    String password;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = OnLoginTelegram.class
    )
    @NotBlank(
            message = "Email не может быть пустым",
            groups = OnRegister.class
    )
    @Email(
            message = "Некорректный формат email",
            groups = {OnRegister.class, OnTelegramPostRegister.class, OnUpdate.class, OnLogin.class}
    )
    String email;


    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnLogin.class, OnLoginTelegram.class}
    )
    @NotBlank(
            message = "Подтверждение пароля не может быть пустым",
            groups = OnRegister.class
    )
    @Size(
            min = 6,
            max = 100,
            message = "Подтверждение пароля должно быть от 6 до 100 символов",
            groups = {OnRegister.class, OnTelegramPostRegister.class, OnUpdate.class, OnLogin.class}
    )
    String passwordConfirm;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnLogin.class, OnRegister.class, OnLoginTelegram.class}
    )
    Set<Role> roles;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnLogin.class, OnRegister.class}
    )
    @Size(
            min = 5,
            max = 5,
            message = "Код должен быть 5 символов длиной",
            groups = OnLoginTelegram.class
    )
    String telegramPasscode;


}

