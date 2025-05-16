package com.ugrasu.bordexback.auth.controller;

import com.ugrasu.bordexback.auth.dto.AuthDto;
import com.ugrasu.bordexback.auth.dto.TelegramLoginInitDataRequest;
import com.ugrasu.bordexback.auth.dto.Tokens;
import com.ugrasu.bordexback.auth.dto.validation.OnLogin;
import com.ugrasu.bordexback.auth.dto.validation.OnLoginTelegram;
import com.ugrasu.bordexback.auth.dto.validation.OnRegister;
import com.ugrasu.bordexback.auth.mapper.AuthMapper;
import com.ugrasu.bordexback.auth.security.JwtCookieComponent;
import com.ugrasu.bordexback.auth.security.authenfication.AuthenticatedUser;
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

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthMapper authMapper;
    private final AuthService authService;
    private final UserService userService;
    private final JwtCookieComponent jwtCookieComponent;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@RequestBody @Validated(OnRegister.class) AuthDto authDto) {
        User user = authMapper.toEntity(authDto);
        User savedUser = authService.register(user);
        return authMapper.toDto(savedUser);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("isAuthenticated()")
    public UserDto me(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return authMapper.toDto(userService.findOne(authenticatedUser.getUserId()));
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody @Validated(OnLogin.class) AuthDto authDto, HttpServletResponse response) {
        User user = authMapper.toEntity(authDto);
        Tokens tokens = authService.login(user);
        return jwtCookieComponent.addJwtSetCookies(response, tokens);
    }

    @PostMapping("/login/telegram-webapp")
    public ResponseEntity<Void> loginTelegramWebApp(@RequestBody TelegramLoginInitDataRequest loginRequest, HttpServletResponse response) {
        boolean isCorrect = authService.validateInitData(loginRequest.getInitData());
        if (!isCorrect) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        User user = authMapper.toEntity(loginRequest);
        User telegramUser = authService.autheficateTelegramUser(user);
        Tokens tokens = authService.loginTelegram(telegramUser);
        return jwtCookieComponent.addJwtSetCookies(response, tokens);
    }

    @PostMapping("/login/telegram")
    public ResponseEntity<Void> loginTelegram(@RequestBody @Validated(OnLoginTelegram.class) AuthDto authDto, HttpServletResponse response) {
        User user = authMapper.toEntity(authDto);
        Tokens tokens = authService.loginTelegram(user);
        return jwtCookieComponent.addJwtSetCookies(response, tokens);
    }

    @PostMapping("/telegram-assign")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, String>> assignTelegram(@AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        String telegramPasscode = authService.generateTelegramPasscode(authenticatedUser.getUserId());
        return ResponseEntity.ok(Map.of("telegramPasscode", telegramPasscode));
    }

    @PostMapping("/telegram-post-register")
    @PreAuthorize("isAuthenticated()")
    public UserDto telegramPostRegister(@AuthenticationPrincipal AuthenticatedUser authenticatedUser,
                                        @RequestBody @Validated(OnRegister.class) AuthDto authDto) {
        User user = authMapper.toEntity(authDto);
        User postRegisterUser = authService.telegramPostRegister(authenticatedUser.getUserId(), user);
        return authMapper.toDto(postRegisterUser);
    }

    @PostMapping("/telegram-unassign")
    @PreAuthorize("isAuthenticated()")
    public UserDto unassignTelegram(HttpServletResponse response,
                                    @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        User user = authService.unassignTelegram(authenticatedUser.getUserId());
        Tokens tokens = authService.login(authenticatedUser);
        jwtCookieComponent.addJwtSetCookies(response, tokens);
        return authMapper.toDto(user);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        return jwtCookieComponent.addJwtLogoutCookies(response);
    }
}
