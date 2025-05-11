package com.ugrasu.bordexback.telegram.handler.impl;

import com.ugrasu.bordexback.telegram.command.Command;
import com.ugrasu.bordexback.telegram.handler.CommandHandler;
import com.ugrasu.bordexback.telegram.sender.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.ugrasu.bordexback.telegram.command.Command.NOT_SUPPORTED;

@Component
@RequiredArgsConstructor
public class NotSupportedCommandHandler implements CommandHandler {

    private final MessageSender messageSender;

    @Override
    public Command getCommand() {
        return NOT_SUPPORTED;
    }

    @Override
    public void handleCommand(Update update, Message message, CallbackQuery callbackQuery, User user) {
        Long chatId = message.getChatId();
        String text = "Command not supported";
        messageSender.sendMessage(chatId, text);
    }
}
