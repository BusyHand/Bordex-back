package com.ugrasu.bordexback.telegram.template;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

@Component
public class MessageTemplate {

    public SendMessage message(Long chatId, String messageText) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .build();
    }

    public SendMessage message(Long chatId, String messageText, String parseMode) {
        return SendMessage.builder()
                .chatId(chatId)
                .text(messageText)
                .parseMode(parseMode)
                .build();
    }
}
