package com.ugrasu.bordexback.telegram.handler.impl;

import com.ugrasu.bordexback.rest.repository.UserRepository;
import com.ugrasu.bordexback.telegram.command.Command;
import com.ugrasu.bordexback.telegram.command.Param;
import com.ugrasu.bordexback.telegram.command.ParsedCommand;
import com.ugrasu.bordexback.telegram.handler.CommandHandler;
import com.ugrasu.bordexback.telegram.sender.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import static com.ugrasu.bordexback.telegram.command.Command.START_ASSIGN_ACCOUNT;

@Component
@RequiredArgsConstructor
public class StartAssignAccountCommandHandlerImpl implements CommandHandler {

    private final MessageSender messageSender;

    //todo refactor
    private final UserRepository userRepository;

    @Override
    public Command getCommand() {
        return START_ASSIGN_ACCOUNT;
    }

    @Override
    public void handleCommand(Update update, Message message, CallbackQuery callbackQuery, User telegramUser, ParsedCommand parsedCommand) {
        Long chatId = message.getChatId();
        if (userRepository.existsByChatId(chatId)) {
            messageSender.sendMessage(chatId, "Телеграм аккаунт уже привязан к другому аккаунту, отвяжите его, и тогда сможете добавить его к этому аккаунту");
            return;
        }
        String telegramPasscode = parsedCommand.getParam(Param.TOKEN);
        userRepository.findByTelegramPasscode(telegramPasscode)
                .ifPresentOrElse((user) -> successAssign(telegramUser, user, chatId),
                        () -> messageSender.sendMessage(chatId, "Код не действителен"));
    }

    private void successAssign(User telegramUser, com.ugrasu.bordexback.rest.entity.User user, Long chatId) {
        user.setChatId(chatId);
        user.setTelegramUsername(telegramUser.getUserName());
        userRepository.save(user);
        messageSender.sendMessage(chatId, "Телеграм аккаунт успешно прикреплен");
    }
}
