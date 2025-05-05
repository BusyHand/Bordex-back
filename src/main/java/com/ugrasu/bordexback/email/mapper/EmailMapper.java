package com.ugrasu.bordexback.email.mapper;

import com.ugrasu.bordexback.email.domain.Email;
import com.ugrasu.bordexback.notification.dto.event.NotificationEventDto;
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

    default Set<Email> toEntities(NotificationEventDto notificationEventDto) {
        EventType eventType = notificationEventDto.getEventType();
        String title = notificationEventDto.getTitle();
        String subject = "Событие:" + eventType + "\n" + notificationEventDto.getContent();
        return notificationEventDto.getConsumers()
                .stream()
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

