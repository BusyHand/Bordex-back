package com.ugrasu.bordexback.notification.dto.event;

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
public class NotificationPayload {

    Long id;

    String title;

    String content;

    Long boardId;

    EventType eventType;

    Set<ConsumerPayload> consumers = new LinkedHashSet<>();

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

}
