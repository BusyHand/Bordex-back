package com.ugrasu.bordexback.rest.entity;


import com.ugrasu.bordexback.rest.entity.enums.Priority;
import com.ugrasu.bordexback.rest.entity.enums.Status;
import com.ugrasu.bordexback.rest.entity.enums.Tag;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tasks")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Task extends BaseEntity {

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    Status status = Status.NEW;

    @Column(name = "priority")
    @Enumerated(value = EnumType.STRING)
    Priority priority;

    @Column(name = "deadline")
    LocalDateTime deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    Board board;

    @Enumerated(EnumType.STRING)
    @Column(name = "tag")
    Tag tag;

    @ManyToMany
    @JoinTable(name = "tasks_users",
            joinColumns = @JoinColumn(name = "task_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id"))
    Set<User> assignees = new LinkedHashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Task task = (Task) o;
        return getId() != null && Objects.equals(getId(), task.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
