package com.ugrasu.bordexback.rest.entity;

import com.ugrasu.bordexback.rest.entity.enums.Role;
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
@Table(name = "_users")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity {

    @Column(
            name = "chat_id",
            unique = true
    )
    Long chatId;

    @Column(
            name = "telegram_username",
            unique = true
    )
    String telegramUsername;

    @Column(
            name = "telegram_passcode",
            unique = true
    )
    String telegramPasscode;

    @Column(
            name = "username",
            unique = true
    )
    String username;

    @Column(
            name = "email",
            unique = true
    )
    String email;

    @Column(name = "password")
    String password;

    @Column(name = "first_name")
    String firstName;

    @Column(name = "last_name")
    String lastName;

    @Column(name = "block")
    boolean block = false;

    @Column(name = "role")
    @Enumerated(value = EnumType.STRING)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "owner_id")
    )
    Set<Role> roles = new LinkedHashSet<>(List.of(Role.USER));

    @OneToMany(
            mappedBy = "owner",
            orphanRemoval = true
    )
    Set<Task> ownTasks = new LinkedHashSet<>();

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    Set<Board> ownBoards = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "assignees")
    Set<Task> assigneesTask = new LinkedHashSet<>();

    @ManyToMany(mappedBy = "boardMembers")
    Set<Board> memberBoards = new LinkedHashSet<>();

    @OneToMany(
            mappedBy = "user",
            orphanRemoval = true
    )
    Set<BoardRoles> boardsRoles = new LinkedHashSet<>();

    public void addBoardRoles(BoardRoles boardRoles) {
        this.boardsRoles.add(boardRoles);
        boardRoles.setUser(this);
    }

    public void removeBoardRoles(BoardRoles boardRoles) {
        this.boardsRoles.remove(boardRoles);
        boardRoles.setUser(null);
    }

    public void deleteOwnBoard(Board board) {
        this.ownBoards.remove(board);
        board.setOwner(null);
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy proxy ? proxy.getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
