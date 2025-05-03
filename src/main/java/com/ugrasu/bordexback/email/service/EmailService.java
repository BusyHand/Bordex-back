package com.ugrasu.bordexback.email.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public void run(String... args) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("cool908yan@yandex.ru");
        message.setTo("cool908yan@yandex.ru");
        message.setSubject("Test Subject");
        message.setText("test text");
        mailSender.send(message);
    }
}
