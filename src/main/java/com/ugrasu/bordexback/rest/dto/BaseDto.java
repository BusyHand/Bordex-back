package com.ugrasu.bordexback.rest.dto;


import com.ugrasu.bordexback.rest.dto.validation.OnCreate;
import com.ugrasu.bordexback.rest.dto.validation.OnUpdate;
import jakarta.validation.constraints.Null;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public abstract class BaseDto {

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    LocalDateTime createdAt;

    @Null(
            message = "Пользователь не имеет права задавать это поле",
            groups = {OnCreate.class, OnUpdate.class}
    )
    LocalDateTime updatedAt;
}
