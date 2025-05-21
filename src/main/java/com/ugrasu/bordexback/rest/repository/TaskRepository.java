package com.ugrasu.bordexback.rest.repository;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Set;

public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @Modifying
    @Query("delete from Task t where t.id = :id")
    void deleteTaskById(@Param("id")Long id);

    @Query("SELECT COALESCE(AVG(t.progress), 0) FROM Task t WHERE t.board = :board")
    int calculateAverageProgressByBoard(@Param("board") Board board);

    Set<Task> findByStatus(Status status);

    Set<Task> findByStatusAndBoard_Id(Status status, Long boardId);
}