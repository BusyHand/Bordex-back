package com.ugrasu.bordexback.auth.dto;

import com.ugrasu.bordexback.auth.dto.validation.OnLogin;
import com.ugrasu.bordexback.auth.dto.validation.OnRegister;
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

    @NotBlank(
            message = "Имя пользователя не может быть пустым",
            groups = {OnLogin.class, OnRegister.class}
    )
    @Size(
            min = 3,
            max = 50,
            message = "Имя пользователя должно быть от 3 до 50 символов",
            groups = {OnLogin.class, OnRegister.class}
    )
    String username;

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

    @NotBlank(
            message = "Пароль не может быть пустым",
            groups = {OnLogin.class, OnRegister.class}
    )
    @Size(
            min = 6,
            max = 100,
            message = "Пароль должен быть от 6 до 100 символов",
            groups = {OnLogin.class, OnRegister.class}
    )
    String password;

    @NotBlank(
            message = "Email не может быть пустым",
            groups = OnRegister.class
    )
    @Email(
            message = "Некорректный формат email",
            groups = OnRegister.class
    )
    String email;


    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnLogin.class}
    )
    @NotBlank(
            message = "Подтверждение пароля не может быть пустым",
            groups = OnRegister.class
    )
    @Size(
            min = 6,
            max = 100,
            message = "Подтверждение пароля должно быть от 6 до 100 символов",
            groups = OnRegister.class
    )
    String passwordConfirm;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnLogin.class, OnRegister.class}
    )
    Set<Role> roles;

}

