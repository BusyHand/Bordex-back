package com.ugrasu.bordexback.telegram.bot;

import com.ugrasu.bordexback.telegram.roter.CommandRouter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.longpolling.BotSession;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.longpolling.starter.AfterBotRegistration;
import org.telegram.telegrambots.longpolling.starter.SpringLongPollingBot;
import org.telegram.telegrambots.longpolling.util.LongPollingSingleThreadUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@RequiredArgsConstructor
@Slf4j
public class BordexBot implements SpringLongPollingBot, LongPollingSingleThreadUpdateConsumer {

    private final CommandRouter commandRouter;

    @Value("${telegrambots.token}")
    private String botToken;

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public LongPollingUpdateConsumer getUpdatesConsumer() {
        return this;
    }

    @Override
    public void consume(Update update) {
        commandRouter.route(update, update.getMessage(), update.getCallbackQuery(), update.getMessage().getFrom());
    }

    @AfterBotRegistration
    public void afterRegistration(BotSession botSession) {
        if (botSession.isRunning()) {
            log.info("Registered bot running state is: {}", botSession.isRunning());
        } else {
            log.error("Registered bot running state is: {}", botSession.isRunning());
        }
    }
}
