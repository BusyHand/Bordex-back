package com.ugrasu.bordexback.auth.security.provider;

import com.ugrasu.bordexback.auth.security.authenfication.TelegramUserAuthentication;
import com.ugrasu.bordexback.auth.service.CustomUserDetailService;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramAuthenticationProvider implements AuthenticationProvider {

    private final UserService userService;
    private final CustomUserDetailService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) {
        User user;
        Object principal = authentication.getPrincipal();
        if (principal instanceof String telegramPasscode) {
            user = userService.findOneByTelegramPasscode(telegramPasscode);
        } else if (principal instanceof Long chatId) {
            user = userService.findOneByChatId(chatId);
        } else {
            throw new AccessDeniedException("Invalid Telegram Password");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getTelegramUsername());
        return new TelegramUserAuthentication(userDetails, userDetails.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return TelegramUserAuthentication.class.isAssignableFrom(authentication);
    }
}
