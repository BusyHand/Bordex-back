package com.ugrasu.bordexback.rest.controller;


import com.ugrasu.bordexback.rest.controller.filter.UserFilter;
import com.ugrasu.bordexback.rest.dto.full.UserDto;
import com.ugrasu.bordexback.rest.dto.validation.OnCreate;
import com.ugrasu.bordexback.rest.dto.validation.OnUpdate;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.mapper.impl.UserMapper;
import com.ugrasu.bordexback.rest.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

    @GetMapping
    @Operation(summary = "Получить список пользователей", description = "Возвращает постраничный список пользователей с фильтрацией по username, email, role и block")
    public PagedModel<UserDto> findAll(@ParameterObject @ModelAttribute UserFilter filter,
                                       @ParameterObject Pageable pageable) {
        Specification<User> specification = filter.toSpecification();
        Page<User> users = userService.findAll(specification, pageable);
        Page<UserDto> userDtos = users.map(userMapper::toDto);
        return new PagedModel<>(userDtos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по его идентификатору")
    public UserDto findById(@PathVariable("id") Long id) {
        User user = userService.findOne(id);
        return userMapper.toDto(user);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать пользователя", description = "Создаёт нового пользователя")
    public UserDto save(@Validated(OnCreate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        return userMapper.toDto(userService.save(user));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Обновить пользователя", description = "Частично обновляет данные пользователя по ID")
    public UserDto update(@PathVariable("id") Long id, @Validated(OnUpdate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User patched = userService.patch(id, user);
        return userMapper.toDto(patched);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя по его идентификатору")
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

}

