package com.ugrasu.bordexback.notification.service;

import com.ugrasu.bordexback.notification.entity.Notification;
import com.ugrasu.bordexback.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public Notification save(Notification notification) {
        return notificationRepository.save(notification);
    }
}
