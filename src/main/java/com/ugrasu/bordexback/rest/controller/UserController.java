package com.ugrasu.bordexback.rest.controller;


import com.ugrasu.bordexback.rest.controller.filter.UserFilter;
import com.ugrasu.bordexback.rest.controller.validation.OnUpdate;
import com.ugrasu.bordexback.rest.dto.web.full.UserDto;
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

@Tag(
        name = "Пользователи",
        description = "Управление пользователями"
)
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(
            summary = "Получить список пользователей",
            description = "Возвращает постраничный список пользователей с фильтрацией по username, email, role и block"
    )
    @GetMapping
    public PagedModel<UserDto> findAll(@ParameterObject @ModelAttribute UserFilter filter,
                                       @ParameterObject Pageable pageable) {
        Specification<User> specification = filter.toSpecification();
        Page<User> users = userService.findAll(specification, pageable);
        Page<UserDto> userDtos = users.map(userMapper::toDto);
        return new PagedModel<>(userDtos);
    }

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает пользователя по его идентификатору"
    )
    @GetMapping("/{id}")
    public UserDto findById(@PathVariable("id") Long id) {
        User user = userService.findOne(id);
        return userMapper.toDto(user);
    }

    @Operation(
            summary = "Обновить пользователя",
            description = "Частично обновляет данные пользователя по ID"
    )
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public UserDto update(@PathVariable("id") Long id, @Validated(OnUpdate.class) @RequestBody UserDto userDto) {
        User user = userMapper.toEntity(userDto);
        User patched = userService.patch(id, user);
        return userMapper.toDto(patched);
    }

    @Operation(
            summary = "Удалить пользователя",
            description = "Удаляет пользователя по его идентификатору"
    )
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        userService.delete(id);
    }

}

