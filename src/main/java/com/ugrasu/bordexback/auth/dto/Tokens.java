package com.ugrasu.bordexback.auth.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tokens {

    String username;
    String accessToken;
    String refreshToken;
    Long accessTokenExpires;
    Long refreshTokenExpires;
}
