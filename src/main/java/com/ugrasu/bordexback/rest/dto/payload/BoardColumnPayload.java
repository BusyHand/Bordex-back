package com.ugrasu.bordexback.rest.dto.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.rest.dto.BaseDto;
import com.ugrasu.bordexback.rest.entity.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.FieldDefaults;

@Data
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Полная информация о доске")
public class BoardColumnPayload extends BaseDto {

    Long id;

    Long columnNumber;

    Status status;

}
