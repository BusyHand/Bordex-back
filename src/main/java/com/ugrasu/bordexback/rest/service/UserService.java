package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.mapper.impl.UserMapper;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public Page<User> findAll(Specification<User> specification, Pageable pageable) {
        return userRepository.findAll(specification, pageable);
    }

    public User findOne(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User with this %s id not found".formatted(id)));
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User patch(Long id, User newUser) {
        User oldUser = findOne(id);
        User updatedUser = userMapper.partialUpdate(newUser, oldUser);
        return userRepository.save(updatedUser);
    }

    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
