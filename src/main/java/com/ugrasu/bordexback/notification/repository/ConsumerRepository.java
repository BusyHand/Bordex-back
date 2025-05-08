package com.ugrasu.bordexback.notification.repository;

import com.ugrasu.bordexback.notification.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface ConsumerRepository extends JpaRepository<Consumer, Long>, JpaSpecificationExecutor<Consumer> {
    Optional<Consumer> findByUserId(Long userId);

    List<Consumer> findAllByUserId(Long userId);
}