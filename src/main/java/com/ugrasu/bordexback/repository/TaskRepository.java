package com.ugrasu.bordexback.repository;

import com.ugrasu.bordexback.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}