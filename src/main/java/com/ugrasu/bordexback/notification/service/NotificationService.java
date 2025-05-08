package com.ugrasu.bordexback.notification.service;

import com.ugrasu.bordexback.notification.entity.Consumer;
import com.ugrasu.bordexback.notification.entity.Notification;
import com.ugrasu.bordexback.notification.publisher.NotificationPublisher;
import com.ugrasu.bordexback.notification.repository.ConsumerRepository;
import com.ugrasu.bordexback.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ConsumerRepository consumerRepository;
    private final NotificationPublisher notificationPublisher;

    public Page<Notification> findAll(Specification<Notification> specification, Pageable pageable) {
        return notificationRepository.findAll(specification, pageable);
    }

    public Notification save(Notification notification) {
        if (notification.getId() != null) {
            notification.setId(null);
        }
        Notification saved = notificationRepository.save(notification);
        return notificationPublisher.publish(saved);
    }

    @Transactional
    public void delete(Long notificationId, Long userId) {
        Notification notification = notificationRepository.findById(notificationId).orElseThrow();
        List<Consumer> consumers = consumerRepository.findAllByUserId(userId);

        consumers.forEach(notification::delete);

        if (notification.getConsumers().isEmpty()) {
            notificationRepository.delete(notification);
        }
        consumers.forEach(consumer -> {
            if (consumer.getNotifications().isEmpty()) {
                consumerRepository.delete(consumer);
            }
        });
    }
}
