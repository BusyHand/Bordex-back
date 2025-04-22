package com.ugrasu.bordexback.dto.slim;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Value;

/**
 * DTO for {@link com.ugrasu.bordexback.entity.User}
 */

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSlimDto {
}