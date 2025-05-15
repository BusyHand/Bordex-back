package com.ugrasu.bordexback.email.mapper;

import com.ugrasu.bordexback.email.domain.Email;
import com.ugrasu.bordexback.notification.dto.event.ConsumerPayload;
import com.ugrasu.bordexback.notification.dto.event.NotificationPayload;
import com.ugrasu.bordexback.rest.event.EventType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface EmailMapper {

    default Set<Email> toEntities(NotificationPayload notificationPayload) {
        EventType eventType = notificationPayload.getEventType();
        String title = notificationPayload.getTitle();
        String subject = "Событие:" + eventType.getTitle() + "\n" + notificationPayload.getContent();
        return notificationPayload.getConsumers()
                .stream()
                .filter(ConsumerPayload::getAllowEmailNotifications)
                .map(user -> {
                    Email email = new Email();
                    email.setTo(user.getEmail());
                    email.setTitle(title);
                    email.setBody(subject);
                    return email;
                })
                .collect(Collectors.toSet());
    }

}

