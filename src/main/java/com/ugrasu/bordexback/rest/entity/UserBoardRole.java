package com.ugrasu.bordexback.rest.entity;

import com.ugrasu.bordexback.notification.entity.Notification;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_board_roles")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserBoardRole extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    Board board;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "board_role")
    @CollectionTable(
            name = "board_role",
            joinColumns = @JoinColumn(name = "user_id")
    )
    @Enumerated(value = EnumType.STRING)
    Set<BoardRole> boardRoles = new LinkedHashSet<>(List.of(BoardRole.VIEWER));

    @OneToMany(
            mappedBy = "userBoardRole",
            orphanRemoval = true
    )
    Set<Notification> notifications = new LinkedHashSet<>();

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        UserBoardRole that = (UserBoardRole) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
