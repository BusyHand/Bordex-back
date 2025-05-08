package com.ugrasu.bordexback.auth.controller;

import com.ugrasu.bordexback.auth.dto.AuthDto;
import com.ugrasu.bordexback.auth.dto.Tokens;
import com.ugrasu.bordexback.auth.dto.validation.OnLogin;
import com.ugrasu.bordexback.auth.dto.validation.OnRegister;
import com.ugrasu.bordexback.auth.mapper.AuthMapper;
import com.ugrasu.bordexback.auth.security.AuthenficatedUser;
import com.ugrasu.bordexback.auth.service.AuthService;
import com.ugrasu.bordexback.rest.dto.web.full.UserDto;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthMapper authMapper;
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@RequestBody @Validated(OnRegister.class) AuthDto authDto) {
        User user = authMapper.toEntity(authDto);
        User savedUser = authService.register(user);
        return authMapper.toDto(savedUser);
    }

    //todo refactor
    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public UserDto me(@AuthenticationPrincipal AuthenficatedUser authenficatedUser) {
        return authMapper.toDto(userService.findOne(authenficatedUser.getUserId()));
    }

    //todo refactor
    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Validated(OnLogin.class) AuthDto authDto, HttpServletResponse response) {
        User user = authMapper.toEntity(authDto);
        Tokens tokens = authService.login(user);

        addJwtCookies(response, tokens);
        return ResponseEntity.ok().build();
    }

    //todo refactor
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        addJwtLogoutCookies(response);
        return ResponseEntity.ok().build();
    }

    //todo refactor
    private void addJwtLogoutCookies(HttpServletResponse response) {
        String accessCookie = buildSetCookieHeader("access_token", "", 0);
        String refreshCookie = buildSetCookieHeader("refresh_token", "", 0);

        response.addHeader("Set-Cookie", accessCookie);
        response.addHeader("Set-Cookie", refreshCookie);
    }

    //todo refactor
    @PostMapping("/refresh")
    public ResponseEntity<Void> refresh(@AuthenticationPrincipal AuthenficatedUser user, HttpServletResponse response) {
        Tokens tokens = authService.refresh(user);
        addJwtCookies(response, tokens);
        return ResponseEntity.ok().build();
    }

    //todo refactor
    private void addJwtCookies(HttpServletResponse response, Tokens tokens) {
        String accessCookie = buildSetCookieHeader("access_token", tokens.getAccessToken(), 3600);
        String refreshCookie = buildSetCookieHeader("refresh_token", tokens.getRefreshToken(), 604800);

        response.addHeader("Set-Cookie", accessCookie);
        response.addHeader("Set-Cookie", refreshCookie);
    }

    //todo refactor
    private String buildSetCookieHeader(String name, String value, int maxAge) {
        return String.format(
                "%s=%s; Max-Age=%d; Path=/; HttpOnly; Secure; SameSite=None",
                name, value, maxAge
        );
    }
}
