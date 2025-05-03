package com.ugrasu.bordexback.notification.service;

import com.ugrasu.bordexback.notification.entity.Notification;
import com.ugrasu.bordexback.notification.publisher.NotificationPublisher;
import com.ugrasu.bordexback.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationPublisher notificationPublisher;

    public Notification save(Notification notification) {
        return notificationPublisher.publish(
                notificationRepository.save(notification));
    }

    public Page<Notification> findAll(Specification<Notification> specification, Pageable pageable) {
        return notificationRepository.findAll(specification, pageable);
    }

    public void delete(Long id) {
        notificationRepository.deleteById(id);
    }
}
