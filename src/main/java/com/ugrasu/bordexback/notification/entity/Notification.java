package com.ugrasu.bordexback.notification.entity;

import com.ugrasu.bordexback.rest.entity.BaseEntity;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.event.EventType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "notifications")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification extends BaseEntity {

    String title;

    String content;

    Long boardId;

    EventType eventType;

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinTable(
            name = "notifications_consumers",
            joinColumns = @JoinColumn(name = "notification_id"),
            inverseJoinColumns = @JoinColumn(name = "consumers_id")
    )
    Set<Consumer> consumers = new LinkedHashSet<>();

    public void delete(Consumer consumer) {
        consumers.remove(consumer);
        consumer.getNotifications().remove(this);
    }

}
