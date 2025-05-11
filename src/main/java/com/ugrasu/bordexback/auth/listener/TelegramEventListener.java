package com.ugrasu.bordexback.auth.listener;

import com.ugrasu.bordexback.auth.service.AuthService;
import com.ugrasu.bordexback.rest.dto.payload.UserPayload;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.mapper.EventMapper;
import com.ugrasu.bordexback.telegram.event.TelegramUserEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TelegramEventListener {

    private final AuthService authService;
    private final EventMapper eventMapper;

    @EventListener
    public void handleTaskEvent(TelegramUserEvent telegramUserEvent) {
        UserPayload telegramUserPayload = telegramUserEvent.getUserpayload();
        User user = eventMapper.toEntity(telegramUserPayload);
        authService.autheficateTelegramUser(user);
    }
}
