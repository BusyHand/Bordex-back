package com.ugrasu.bordexback.rest.repository;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.UserBoardRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserBoardRoleRepository extends JpaRepository<UserBoardRole, Long>, JpaSpecificationExecutor<UserBoardRole> {
    Optional<UserBoardRole> findByUser_IdAndBoard_Id(Long userId, Long boardId);

    @Transactional
    @Modifying
    void deleteByUser_IdAndBoard_Id(Long userId, Long boardId);
}