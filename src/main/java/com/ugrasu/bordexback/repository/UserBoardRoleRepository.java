package com.ugrasu.bordexback.repository;

import com.ugrasu.bordexback.entity.UserBoardRole;
import com.ugrasu.bordexback.entity.enums.BoardRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

public interface UserBoardRoleRepository extends JpaRepository<UserBoardRole, Long> {
    Optional<UserBoardRole> findByUser_IdAndBoard_Id(Long userId, Long boardId);

}