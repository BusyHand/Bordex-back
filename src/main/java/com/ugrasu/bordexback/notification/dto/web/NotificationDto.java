package com.ugrasu.bordexback.notification.dto.web;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.notification.dto.event.ConsumerEventDto;
import com.ugrasu.bordexback.rest.dto.BaseDto;
import com.ugrasu.bordexback.rest.event.EventType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Полная информация о уведомление")
public class NotificationDto extends BaseDto {

    Long id;

    String title;

    String content;

    String link;

    EventType eventType;

}
