package com.ugrasu.bordexback.rest.entity;

import com.ugrasu.bordexback.rest.entity.enums.Scope;
import com.ugrasu.bordexback.rest.entity.enums.Status;
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

    @Column(name = "progress")
    Integer progress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "owner_id",
            nullable = false
    )
    User owner;

    @OneToMany(
            mappedBy = "board",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    Set<Task> tasks = new LinkedHashSet<>();

    @OneToMany(
            mappedBy = "board",
            orphanRemoval = true
    )
    Set<BoardRoles> boardRoles = new LinkedHashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_boards",
            joinColumns = @JoinColumn(name = "board_id"),
            inverseJoinColumns = @JoinColumn(name = "users_id")
    )
    Set<User> boardMembers = new LinkedHashSet<>();

    @OneToMany(
            mappedBy = "board",
            orphanRemoval = true
    )
    Set<BoardColumn> boardColumns = new LinkedHashSet<>();

    public void addBoardRoles(BoardRoles boardRoles) {
        if (this.boardRoles == null) {
            this.boardRoles = new LinkedHashSet<>();
        }
        this.boardRoles.add(boardRoles);
        boardRoles.setBoard(this);
    }

    public void removeBoardRoles(BoardRoles boardRoles) {
        this.boardRoles.remove(boardRoles);
        boardRoles.setBoard(null);
    }

    public void addMember(User user) {
        if (this.boardMembers == null) {
            this.boardMembers = new LinkedHashSet<>();
        }
        this.boardMembers.add(user);
        user.getMemberBoards().add(this);
    }

    public void removeMember(User user) {
        this.boardMembers.remove(user);
        user.getMemberBoards().remove(this);
    }

    public boolean isMember(User newOwner) {
        return this.boardMembers.contains(newOwner);
    }

    public void deleteAll() {
        this.boardRoles.forEach(boardRoles -> boardRoles.setBoard(null));
        this.boardRoles.clear();

        this.boardMembers.forEach(boardMember -> boardMember.getMemberBoards().remove(this));
        this.boardMembers.clear();

        this.owner.deleteOwnBoard(this);
    }

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
