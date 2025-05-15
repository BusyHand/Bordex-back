package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.rest.controller.filter.UserFilter;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.mapper.impl.UserMapper;
import com.ugrasu.bordexback.rest.publisher.EventPublisher;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ugrasu.bordexback.rest.event.EventType.USER_DELETED;
import static com.ugrasu.bordexback.rest.event.EventType.USER_UPDATE;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EventPublisher eventPublisher;

    public Page<User> findAll(UserFilter userFilter, Pageable pageable) {
        return userRepository.findAll(userFilter.filter(), pageable);
    }

    public User findOne(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with this %s id not found".formatted(id)));
    }

    public User findOne(String usernameOrEmailOrTelegramUsername) {
        return userRepository.findUserByUsernameOrEmailOrTelegramUsername(usernameOrEmailOrTelegramUsername, usernameOrEmailOrTelegramUsername, usernameOrEmailOrTelegramUsername)
                .orElseThrow(() -> new EntityNotFoundException("User with this %s username or email not found".formatted(usernameOrEmailOrTelegramUsername)));
    }

    public User findOneByTelegramPasscode(String telegramPasscode) {
        return userRepository.findByTelegramPasscode(telegramPasscode)
                .orElseThrow(() -> new EntityNotFoundException("Passcode incorrect"));
    }

    @Transactional
    @PreAuthorize("@use.isTheSameUser(#id)")
    public User patch(@P("id") Long id, User newUser) {
        User oldUser = findOne(id);
        User updatedUser = userMapper.partialUpdate(newUser, oldUser);
        return eventPublisher.publish(USER_UPDATE, updatedUser);
    }

    @Transactional
    @PreAuthorize("@use.isTheSameUser(#id)")
    public void delete(@P("id") Long id) {
        User userToDelete = findOne(id);
        eventPublisher.publish(USER_DELETED, userToDelete);
        userRepository.deleteById(id);
    }
}