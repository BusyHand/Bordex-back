package com.ugrasu.bordexback.rest.dto.payload;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.rest.dto.BaseDto;
import com.ugrasu.bordexback.rest.dto.web.slim.BoardSlimDto;
import com.ugrasu.bordexback.rest.dto.web.slim.UserSlimDto;
import com.ugrasu.bordexback.rest.entity.enums.Priority;
import com.ugrasu.bordexback.rest.entity.enums.Status;
import com.ugrasu.bordexback.rest.entity.enums.Tag;
import com.ugrasu.bordexback.rest.event.EventType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskPayload extends BaseDto {

    Long id;

    String name;

    String description;

    Status status;

    Priority priority;

    Tag tag;

    String tagColor;

    Integer progress;

    LocalDateTime deadline;

    Long columRowNumber;

    UserPayload owner;

    BoardPayload board;

    Set<UserPayload> assignees;

    UserPayload unassignUser;

    EventType eventType;


}
