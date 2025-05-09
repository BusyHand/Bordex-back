package com.ugrasu.bordexback.rest.repository;

import com.ugrasu.bordexback.rest.entity.BoardRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface BoardRolesRepository extends JpaRepository<BoardRoles, Long>, JpaSpecificationExecutor<BoardRoles> {


    Optional<BoardRoles> findByUser_IdAndBoard_Id(Long userId, Long boardId);

    @Transactional
    @Modifying
    void deleteByUser_IdAndBoard_Id(Long userId, Long boardId);
}