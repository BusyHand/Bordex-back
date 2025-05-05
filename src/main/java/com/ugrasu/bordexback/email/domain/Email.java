package com.ugrasu.bordexback.email.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Email {
    String to;
    String title;
    String body;
}
