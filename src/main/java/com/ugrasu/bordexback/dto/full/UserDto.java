package com.ugrasu.bordexback.dto.full;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

/**
 * DTO for {@link com.ugrasu.bordexback.entity.User}
 */
@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {
}