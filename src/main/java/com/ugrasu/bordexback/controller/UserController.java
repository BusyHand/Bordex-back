package com.ugrasu.bordexback.controller;


import com.ugrasu.bordexback.dto.full.UserDto;
import com.ugrasu.bordexback.entity.User;
import com.ugrasu.bordexback.mapper.impl.UserMapper;
import com.ugrasu.bordexback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public PagedModel<UserDto> findAll(Pageable pageable) {
        Page<User> users = userService.findAll(pageable);
        Page<UserDto> userDtos = users.map(userMapper::toDto);
        return new PagedModel<>(userDtos);
    }

    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id) {
        User user = userService.findOne(id);
        return userMapper.toDto(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userService.save(user));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@PathVariable Long id, @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User patched = userService.patch(id, user);
        return userMapper.toDto(patched);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
