package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.mapper.impl.UserMapper;
import com.ugrasu.bordexback.rest.publisher.EventPublisher;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<User> findAll(Specification<User> specification, Pageable pageable) {
        return userRepository.findAll(specification, pageable);
    }

    public User findOne(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with this %s id not found".formatted(id)));
    }

    public User findOne(String usernameOrEmail) {
        return userRepository.findUserByUsernameOrEmailOrTelegramUsername(usernameOrEmail, usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with this %s username or email not found".formatted(usernameOrEmail)));
    }

    public User findOneByTelegramPasscode(String telegramPasscode) {
        return userRepository.findByTelegramPasscode(telegramPasscode)
                .orElseThrow(() -> new EntityNotFoundException("Passcode incorrect"));
    }

    @Transactional
    public User patch(Long id, User newUser) {
        User oldUser = findOne(id);
        User updatedUser = userMapper.partialUpdate(newUser, oldUser);
        return eventPublisher.publish(USER_UPDATE, updatedUser);
    }

    @Transactional
    public void delete(Long id) {
        User userToDelete = findOne(id);
        eventPublisher.publish(USER_DELETED, userToDelete);
        userRepository.deleteById(id);
    }
}