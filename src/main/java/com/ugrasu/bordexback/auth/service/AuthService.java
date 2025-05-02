package com.ugrasu.bordexback.auth.service;

import com.ugrasu.bordexback.auth.config.TokenProvider;
import com.ugrasu.bordexback.auth.dto.Tokens;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;

    public Tokens login(User user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );
        UserDetails loginUser = (UserDetails) authentication.getPrincipal();
        return tokenProvider.generateTokens(loginUser);
    }

    public Tokens refresh(UserDetails user) {
        return tokenProvider.generateTokens(user);
    }

    public User register(User user) {
        return userService.save(user);
    }
}
