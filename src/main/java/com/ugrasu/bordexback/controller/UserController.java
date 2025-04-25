package com.ugrasu.bordexback.controller;


import com.ugrasu.bordexback.dto.full.UserDto;
import com.ugrasu.bordexback.dto.validation.OnCreate;
import com.ugrasu.bordexback.dto.validation.OnUpdate;
import com.ugrasu.bordexback.entity.User;
import com.ugrasu.bordexback.mapper.impl.UserMapper;
import com.ugrasu.bordexback.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Пользователи", description = "Управление пользователями")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(summary = "Получить список пользователей", description = "Возвращает постраничный список пользователей")
    @GetMapping
    public PagedModel<UserDto> findAll(Pageable pageable) {
        Page<User> users = userService.findAll(pageable);
        Page<UserDto> userDtos = users.map(userMapper::toDto);
        return new PagedModel<>(userDtos);
    }

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по его идентификатору")
    @GetMapping("/{id}")
    public UserDto findById(@PathVariable Long id) {
        User user = userService.findOne(id);
        return userMapper.toDto(user);
    }

    @Operation(summary = "Создать пользователя", description = "Создаёт нового пользователя")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto save(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userService.save(user));
    }

    @Operation(summary = "Обновить пользователя", description = "Частично обновляет данные пользователя по ID")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@PathVariable Long id, @Validated(OnUpdate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User patched = userService.patch(id, user);
        return userMapper.toDto(patched);
    }

    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по его идентификатору")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}

