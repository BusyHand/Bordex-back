package com.ugrasu.bordexback.notification.dto;

import com.ugrasu.bordexback.rest.event.EventType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NotificationEventDto {

    Long id;

    String title;

    String content;

    String link;

    EventType eventType;

    Set<Long> consumersIds = new LinkedHashSet<>();

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

}
