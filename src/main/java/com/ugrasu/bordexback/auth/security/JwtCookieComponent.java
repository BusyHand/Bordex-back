package com.ugrasu.bordexback.auth.security;

import com.ugrasu.bordexback.auth.dto.Tokens;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class JwtCookieComponent {

    private static final String COOKIE_SETTINGS = "%s=%s; Max-Age=%d; Path=/; HttpOnly; Secure; SameSite=None";

    private static final Long COOKIE_EXPIRES_NOW = 0L;

    @Value("${jwt.access-token.expiration}")
    private Long accessTokenExpiration;

    @Value("${jwt.refresh-token.expiration}")
    private Long refreshTokenExpiration;


    private String buildSetCookieHeader(String name, String value, Long maxAge) {
        return format(COOKIE_SETTINGS, name, value, maxAge);
    }

    private ResponseEntity<Void> addJwtSetCookies(HttpServletResponse response, String access, String refresh, Long accessTokenExpiration, Long refreshTokenExpiration) {
        String accessCookie = buildSetCookieHeader("access_token", access, accessTokenExpiration);
        String refreshCookie = buildSetCookieHeader("refresh_token", refresh, refreshTokenExpiration);
        response.addHeader("Set-Cookie", accessCookie);
        response.addHeader("Set-Cookie", refreshCookie);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<Void> addJwtSetCookies(HttpServletResponse response, Tokens tokens) {
        return addJwtSetCookies(response, tokens.getAccessToken(), tokens.getRefreshToken(), accessTokenExpiration, refreshTokenExpiration);
    }

    public ResponseEntity<Void> addJwtLogoutCookies(HttpServletResponse response) {
        return addJwtSetCookies(response, "", "", COOKIE_EXPIRES_NOW, COOKIE_EXPIRES_NOW);
    }
}
