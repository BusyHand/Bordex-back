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

    public User findOne(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User with this %s username not found".formatted(username)));
    }

    @Transactional
    public User patch(Long id, User newUser) {
        User oldUser = findOne(id);
        User updatedUser = userMapper.partialUpdate(newUser, oldUser);
        return eventPublisher.publish(USER_UPDATE,
                userRepository.save(updatedUser));
    }

    @Transactional
    public void delete(Long id) {
        User userToDelete = findOne(id);
        userRepository.deleteUserById(id);
        eventPublisher.publish(USER_DELETED, userToDelete);
    }
}
