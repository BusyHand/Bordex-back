package com.ugrasu.bordexback.rest.repository;

import com.ugrasu.bordexback.rest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    boolean existsUserByUsernameOrEmail(String username, String email);

    boolean existsByTelegramUsername(String telegramUsername);

    Optional<User> findByTelegramPasscode(String telegramPasscode);

    Optional<User> findUserByUsernameOrEmailOrTelegramUsername(String username, String email, String telegramUsername);

    boolean existsUserByUsername(String username);

    boolean existsByChatId(Long chatId);

    Optional<User> findByChatId(Long chatId);

    boolean existsByTelegramPasscode(String telegramPasscode);
}