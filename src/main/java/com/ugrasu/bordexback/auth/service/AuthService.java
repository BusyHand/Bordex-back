package com.ugrasu.bordexback.auth.service;

import com.ugrasu.bordexback.auth.dto.Tokens;
import com.ugrasu.bordexback.auth.security.TelegramUserAuthentication;
import com.ugrasu.bordexback.auth.security.TokenProvider;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.Role;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private Tokens login(Authentication toAuthenticate) {
        Authentication authentication = authenticationManager.authenticate(toAuthenticate);
        UserDetails loginUser = (UserDetails) authentication.getPrincipal();
        return tokenProvider.generateTokens(loginUser);
    }

    public Tokens login(User user) {
        return login(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
    }

    public Tokens loginTelegram(User user) {
        return login(new TelegramUserAuthentication(user.getTelegramPasscode()));
    }

    public Tokens refresh(UserDetails user) {
        return tokenProvider.generateTokens(user);
    }

    public User register(User user) {
        if (userRepository.existsUserByUsernameOrEmail(user.getUsername(), user.getEmail())) {
            throw new EntityExistsException("User with username or email already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User autheficateTelegramUser(User user) {
        if (userRepository.existsByChatId(user.getChatId())) {
            User alreadyExistUser = userRepository.findByChatId(user.getChatId()).get();
            if (!alreadyExistUser.getTelegramUsername().equals(user.getTelegramUsername())
                && (userRepository.existsUserByUsername(user.getTelegramUsername())
                 || userRepository.existsByTelegramUsername(user.getTelegramUsername()))) {
                user.setTelegramUsername(user.getTelegramUsername() + UUID.randomUUID());
            }
            alreadyExistUser.setTelegramUsername(user.getTelegramUsername());
            alreadyExistUser.setTelegramPasscode(user.getTelegramPasscode());
            user = alreadyExistUser;

        } else if (userRepository.existsUserByUsername(user.getTelegramUsername())) {
            user.setRoles(Set.of(Role.USER));
            user.setTelegramUsername(user.getTelegramUsername() + UUID.randomUUID());
        }
        return userRepository.save(user);
    }
}
