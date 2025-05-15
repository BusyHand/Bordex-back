package com.ugrasu.bordexback.notification.service;

import com.ugrasu.bordexback.notification.controller.filter.NotificationFilter;
import com.ugrasu.bordexback.notification.entity.Consumer;
import com.ugrasu.bordexback.notification.entity.Notification;
import com.ugrasu.bordexback.notification.publisher.NotificationPublisher;
import com.ugrasu.bordexback.notification.repository.ConsumerRepository;
import com.ugrasu.bordexback.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toSet;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final ConsumerRepository consumerRepository;
    private final NotificationPublisher notificationPublisher;

    @PreAuthorize("@use.isTheSameUser(#filter.userId())")
    public Page<Notification> findAll(@P("filter") NotificationFilter filter, Pageable pageable) {
        return notificationRepository.findAll(filter.filter(), pageable);
    }

    public Notification save(Notification notification) {
        if (notification.getId() != null) {
            notification.setId(null);
        }
       return notificationRepository.save(notification);
    }

    @Transactional
    @PreAuthorize("@use.isTheSameUser(#userId)")
    public void delete(Long notificationId, @P("userId") Long userId) {
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
