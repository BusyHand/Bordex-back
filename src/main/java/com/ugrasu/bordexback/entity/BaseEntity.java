package com.ugrasu.bordexback.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    protected Long id;

    @Column(name = "create_at")
    @EqualsAndHashCode.Exclude
    private LocalDateTime createAt;

    @Column(name = "update_at")
    @EqualsAndHashCode.Exclude
    private LocalDateTime updateAt;

    @Column(name = "deleted", nullable = false)
    @EqualsAndHashCode.Exclude
    private boolean deleted = false;

    @PrePersist
    protected void onCreate() {
        createAt = LocalDateTime.now();
        updateAt = createAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updateAt = LocalDateTime.now();
    }

}
