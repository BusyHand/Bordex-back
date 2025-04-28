package com.ugrasu.bordexback.rest.entity;

import com.ugrasu.bordexback.rest.entity.enums.Scope;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.hibernate.proxy.HibernateProxy;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "boards")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Board extends BaseEntity {

    @Column(name = "name")
    String name;

    @Column(name = "description")
    String description;

    @Column(name = "scope")
    @Enumerated(value = EnumType.STRING)
    Scope scope = Scope.PUBLIC;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    User owner;

    @OneToMany(mappedBy = "board", orphanRemoval = true)
    Set<Task> tasks = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "userBoards")
    Set<User> boardUsers = new LinkedHashSet<>();

    @OneToMany(mappedBy = "board", orphanRemoval = true)
     Set<UserBoardRole> userBoardRoles = new LinkedHashSet<>();


    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Board board = (Board) o;
        return getId() != null && Objects.equals(getId(), board.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
