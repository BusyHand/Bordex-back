package com.ugrasu.bordexback.rest.repository;

import com.ugrasu.bordexback.rest.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    @Transactional
    @Modifying
    @Query(value = "insert into tasks_users (task_id, users_id) values (:taskId, :userId)", nativeQuery = true)
    void assignUserToTask(@Param("taskId") Long taskId, @Param("userId") Long userId);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "DELETE FROM tasks_users WHERE task_id = :taskId AND users_id = :userId", nativeQuery = true)
    void unassignUserFromTask(@Param("taskId") Long taskId, @Param("userId") Long userId);

    @Query(value = "select count(*) > 0 from tasks_users where task_id = :taskId and users_id = :userId", nativeQuery = true)
    boolean existsByTaskIdAndUserId(@Param("taskId") Long taskId, @Param("userId") Long userId);

    void deleteTaskById(Long id);
}