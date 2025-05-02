package com.ugrasu.bordexback.auth.controller;

import com.ugrasu.bordexback.auth.config.AuthenficatedUser;
import com.ugrasu.bordexback.auth.dto.AuthDto;
import com.ugrasu.bordexback.auth.dto.Tokens;
import com.ugrasu.bordexback.auth.dto.validation.OnLogin;
import com.ugrasu.bordexback.auth.dto.validation.OnRegister;
import com.ugrasu.bordexback.auth.mapper.AuthMapper;
import com.ugrasu.bordexback.auth.service.AuthService;
import com.ugrasu.bordexback.rest.dto.web.full.UserDto;
import com.ugrasu.bordexback.rest.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthMapper authMapper;
    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto register(@RequestBody @Validated(OnRegister.class) AuthDto authDto) {
        User user = authMapper.toEntity(authDto);
        User savedUser = authService.register(user);
        return authMapper.toDto(savedUser);
    }

    @PostMapping("/login")
    public Tokens login(@RequestBody @Validated(OnLogin.class) AuthDto authDto) {
        User user = authMapper.toEntity(authDto);
        return authService.login(user);
    }

    @GetMapping("/me")
    @ResponseStatus(HttpStatus.OK)
    public UserDto me(@AuthenticationPrincipal AuthenficatedUser authenficatedUser) {
        return authMapper.toDto(authenficatedUser.getUser());
    }

    @PostMapping("/refresh")
    @ResponseStatus(HttpStatus.OK)
    public Tokens refresh(@AuthenticationPrincipal AuthenficatedUser authenficatedUser) {
        return authService.refresh(authenficatedUser);
    }
}
