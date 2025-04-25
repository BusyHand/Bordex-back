package com.ugrasu.bordexback.dto.slim;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.dto.validation.OnCreate;
import com.ugrasu.bordexback.dto.validation.OnUpdate;
import jakarta.validation.constraints.Null;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserBoardRoleSlimDto {

    @Null(
            message = "ID задается системой",
            groups = {OnCreate.class, OnUpdate.class}
    )
    Long id;
}
