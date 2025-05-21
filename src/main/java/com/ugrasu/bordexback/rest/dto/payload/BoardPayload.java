package com.ugrasu.bordexback.rest.dto.payload;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.rest.dto.BaseDto;
import com.ugrasu.bordexback.rest.entity.enums.Scope;
import com.ugrasu.bordexback.rest.entity.enums.Status;
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
public class BoardPayload extends BaseDto {

    Long id;

    String name;

    String description;

    Scope scope;

    UserPayload owner;

    Long tasksCount;

    Long membersCount;

    Long progress;

    Set<UserPayload> boardMembers;

    EventType eventType;

    UserPayload unassignUser;

    Set<BoardColumnPayload> boardColumns;
}
