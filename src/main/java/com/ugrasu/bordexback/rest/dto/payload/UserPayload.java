package com.ugrasu.bordexback.rest.dto.payload;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.ugrasu.bordexback.rest.dto.BaseDto;
import com.ugrasu.bordexback.rest.entity.enums.Role;
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
public class UserPayload extends BaseDto {

    Long id;

    Long chatId;

    String telegramUsername;

    String telegramPasscode;

    String username;

    String firstName;

    String lastName;

    String email;

    Boolean allowTelegramNotifications;

    Boolean allowEmailNotifications;

    Boolean allowOnSiteNotifications;

    Set<Role> roles;

    EventType eventType;

}
