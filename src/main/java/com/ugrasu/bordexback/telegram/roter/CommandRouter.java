package com.ugrasu.bordexback.telegram.roter;

import com.ugrasu.bordexback.telegram.command.Command;
import com.ugrasu.bordexback.telegram.command.ParsedCommand;
import com.ugrasu.bordexback.telegram.handler.CommandHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.util.Map;

import static com.ugrasu.bordexback.telegram.command.Command.of;

@Component
@RequiredArgsConstructor
public class CommandRouter {

    private final Map<Command, CommandHandler> commandHandlers;

    private CommandHandler getCommandHandler(Command command) {
        return commandHandlers.get(command);
    }

    public void route(Update update, Message message, CallbackQuery callbackQuery, User user) {
        String commandText = message.getText();
        ParsedCommand parsedCommand = of(commandText);
        CommandHandler commandHandler = getCommandHandler(parsedCommand.command());
        commandHandler.handleCommand(update, message, callbackQuery, user, parsedCommand);
    }
}
