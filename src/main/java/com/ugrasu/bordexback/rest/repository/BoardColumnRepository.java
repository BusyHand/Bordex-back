package com.ugrasu.bordexback.rest.repository;

import com.ugrasu.bordexback.rest.entity.BoardColumn;
import com.ugrasu.bordexback.rest.entity.BoardRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface BoardColumnRepository extends JpaRepository<BoardColumn, Long>, JpaSpecificationExecutor<BoardRoles> {

}