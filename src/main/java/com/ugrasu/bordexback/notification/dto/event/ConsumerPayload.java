package com.ugrasu.bordexback.notification.dto.event;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsumerPayload {

    Long id;

    Long chatId;

    Long userId;

    String telegramUsername;

    String username;

    String firstName;

    String lastName;

    String email;

    Boolean allowTelegramNotifications;

    Boolean allowEmailNotifications;

    Boolean allowOnSiteNotifications;
}
