package com.ugrasu.bordexback.rest.entity;

import com.ugrasu.bordexback.rest.entity.enums.Status;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity
@SuperBuilder
@NoArgsConstructor
@Table(
        name = "board_column",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"status", "board_id"})}
)

@FieldDefaults(level = AccessLevel.PRIVATE)
public class BoardColumn extends BaseEntity {

    @Column(
            name = "column_number",
            nullable = false
    )
    Long columnNumber;

    @Column(
            name = "status",
            nullable = false
    )
    @Enumerated(value = EnumType.STRING)
    Status status = Status.NEW;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    Board board;

}
