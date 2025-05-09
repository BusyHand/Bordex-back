package com.ugrasu.bordexback.notification.dto.event;


import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConsumerPayload {

    Long id;


    Long chatId;

    Long userId;

    String username;

    String firstName;

    String lastName;

    String email;
}
