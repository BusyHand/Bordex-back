package com.ugrasu.bordexback.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;

import java.io.Serializable;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@FilterDef(name = "notDeletedFilter")
@Filter(name = "notDeletedFilter", condition = "deleted IS false")
public class BaseEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    protected Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @EqualsAndHashCode.Exclude
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    @EqualsAndHashCode.Exclude
    private LocalDateTime updatedAt;

    @Column(name = "deleted", nullable = false)
    @EqualsAndHashCode.Exclude
    private boolean deleted = false;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
