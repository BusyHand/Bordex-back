package com.ugrasu.bordexback.telegram.handler;

import com.ugrasu.bordexback.telegram.command.Command;
import com.ugrasu.bordexback.telegram.command.ParsedCommand;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface CommandHandler {

    Command getCommand();


    default void handleCommand(Update update, Message message, CallbackQuery callbackQuery, User user, ParsedCommand parsedCommand) {
        handleCommand(message, user);
        handleCommand(message);
    }

    default void handleCommand(Message message, User user) {}

    default void handleCommand(Message message) {}

}
