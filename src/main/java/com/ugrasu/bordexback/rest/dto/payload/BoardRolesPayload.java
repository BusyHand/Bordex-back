package com.ugrasu.bordexback.rest.dto.payload;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.rest.dto.BaseDto;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import com.ugrasu.bordexback.rest.event.EventType;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BoardRolesPayload extends BaseDto {

    Long id;

    UserPayload user;

    BoardPayload board;

    Set<BoardRole> boardRoles;

    EventType eventType;

}
