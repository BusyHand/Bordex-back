package com.ugrasu.bordexback.rest.dto.web.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.rest.controller.validation.OnCreate;
import com.ugrasu.bordexback.rest.controller.validation.OnUpdate;
import com.ugrasu.bordexback.rest.dto.BaseDto;
import com.ugrasu.bordexback.rest.dto.web.slim.UserSlimDto;
import com.ugrasu.bordexback.rest.entity.enums.Scope;
import com.ugrasu.bordexback.rest.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Полная информация о доске")
public class BoardColumnDto extends BaseDto {

    @Null
    Long id;

    @NotNull
    Long columnNumber;

    @NotNull
    Status status;

}
