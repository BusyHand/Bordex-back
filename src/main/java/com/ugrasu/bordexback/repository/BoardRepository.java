package com.ugrasu.bordexback.repository;

import com.ugrasu.bordexback.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}