package com.ugrasu.bordexback.telegram.config;

import com.ugrasu.bordexback.telegram.command.Command;
import com.ugrasu.bordexback.telegram.handler.CommandHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Configuration
@Slf4j
public class BotConfig {

    @Bean
    public TelegramClient telegramClient(@Value("${telegrambots.token}") String botToken) {
        return new OkHttpTelegramClient(botToken);
    }

    @Bean
    public Map<Command, CommandHandler> commandStrategy(List<CommandHandler> commandHandlers) {
        return commandHandlers.stream()
                .collect(Collectors.toMap(CommandHandler::getCommand, identity()));
    }
}
