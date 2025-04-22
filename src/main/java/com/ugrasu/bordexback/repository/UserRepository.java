package com.ugrasu.bordexback.repository;

import com.ugrasu.bordexback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}