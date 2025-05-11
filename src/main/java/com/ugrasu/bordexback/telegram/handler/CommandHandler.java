package com.ugrasu.bordexback.telegram.handler;

import com.ugrasu.bordexback.telegram.command.Command;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

public interface CommandHandler {

    Command getCommand();

    void handleCommand(Update update, Message message, CallbackQuery callbackQuery, User user);

}
