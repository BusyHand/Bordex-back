package com.ugrasu.bordexback.telegram.sender;

import com.ugrasu.bordexback.telegram.template.MessageTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageSender {

    private final TelegramClient telegramClient;
    private final MessageTemplate messageTemplate;

    private void sendMessage(SendMessage sendMessage) {
        try {
            telegramClient.executeAsync(sendMessage);
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }

    public void sendMessage(Long chatId, String text) {
        sendMessage(messageTemplate.message(chatId, text));
    }

    public void sendMessage(Long chatId, String text, String parseMode) {
        sendMessage(messageTemplate.message(chatId, text, parseMode));
    }

    public void sendMessage(List<Long> chatsIds, String text) {
        chatsIds.forEach(chatsId -> sendMessage(messageTemplate.message(chatsId, text)));
    }
}
