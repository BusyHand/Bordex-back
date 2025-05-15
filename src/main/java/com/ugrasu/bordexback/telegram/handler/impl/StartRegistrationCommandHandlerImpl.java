package com.ugrasu.bordexback.telegram.handler.impl;

import com.ugrasu.bordexback.rest.dto.payload.UserPayload;
import com.ugrasu.bordexback.rest.entity.enums.Role;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import com.ugrasu.bordexback.telegram.command.Command;
import com.ugrasu.bordexback.telegram.handler.CommandHandler;
import com.ugrasu.bordexback.telegram.publisher.BotEventPublisher;
import com.ugrasu.bordexback.telegram.sender.MessageSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.message.Message;

import java.security.SecureRandom;
import java.util.Set;

import static com.ugrasu.bordexback.rest.event.EventType.TELEGRAM_REGISTER;

@Component
@RequiredArgsConstructor
public class StartRegistrationCommandHandlerImpl implements CommandHandler {

    private final MessageSender messageSender;
    private final BotEventPublisher botEventPublisher;

    //todo refactor
    private final UserRepository userRepository;

    @Override
    public Command getCommand() {
        return Command.START_REGISTRATION;
    }

    @Override
    public void handleCommand(Message message, User user) {
        String passcode = getRandomPasscode();
        UserPayload userPayload = new UserPayload();
        userPayload.setChatId(message.getChatId());
        userPayload.setTelegramUsername(user.getUserName());
        userPayload.setFirstName(user.getFirstName());
        userPayload.setLastName(user.getLastName());
        userPayload.setTelegramPasscode(passcode);
        userPayload.setAllowEmailNotifications(true);
        userPayload.setAllowTelegramNotifications(true);
        userPayload.setAllowOnSiteNotifications(true);
        userPayload.setRoles(Set.of(Role.USER));
        botEventPublisher.publish(TELEGRAM_REGISTER, userPayload);
        String messageText = """
                *Введите код на сайте*
                
                ```
                      %s
                ```
                """.formatted(passcode);
        messageSender.sendMessage(message.getChatId(), messageText, ParseMode.MARKDOWNV2);
    }

    private String getRandomPasscode() {
        String passcode;
        do {
            passcode = generateRandomPasscode();
        } while (userRepository.existsByTelegramPasscode(passcode));
        return passcode;
    }

    private String generateRandomPasscode() {
        String characters = "0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder passcode = new StringBuilder(5);
        for (int i = 0; i < 5; i++) {
            int index = random.nextInt(characters.length());
            passcode.append(characters.charAt(index));
        }
        return passcode.toString();
    }
}
