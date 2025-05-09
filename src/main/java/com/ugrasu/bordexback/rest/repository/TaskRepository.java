package com.ugrasu.bordexback.rest.repository;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {

    void deleteTaskById(Long id);

    @Query("SELECT COALESCE(AVG(t.progress), 0) FROM Task t WHERE t.board = :board")
    int calculateAverageProgressByBoard(@Param("board") Board board);
}