package com.ugrasu.bordexback.rest.controller;

import com.ugrasu.bordexback.rest.controller.filter.UserBoardRoleFilter;
import com.ugrasu.bordexback.rest.controller.validation.OnUpdate;
import com.ugrasu.bordexback.rest.dto.web.full.UserBoardRoleDto;
import com.ugrasu.bordexback.rest.entity.BoardRoles;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import com.ugrasu.bordexback.rest.mapper.impl.UserBoardRoleMapper;
import com.ugrasu.bordexback.rest.service.BoardRolesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Роли пользователей на досках",
        description = "Управление ролями пользователей на досках"
)
@RestController
@RequestMapping("/api/users/boards/roles")
@RequiredArgsConstructor
@PreAuthorize("isAuthenticated()")
public class UserBoardRoleController {

    private final BoardRolesService boardRolesService;
    private final UserBoardRoleMapper userBoardRoleMapper;

    @Operation(
            summary = "Получить список ролей пользователей на досках",
            description = "Возвращает постраничный список ролей с фильтрацией по userId, boardId и boardRole"
    )
    @GetMapping
    public PagedModel<UserBoardRoleDto> findAll(@ParameterObject @ModelAttribute UserBoardRoleFilter filter,
                                                @ParameterObject Pageable pageable) {
        Specification<BoardRoles> specification = filter.toSpecification();
        Page<BoardRoles> userBoardRoles = boardRolesService.findAll(specification, pageable);
        Page<UserBoardRoleDto> userBoardRoleDtos = userBoardRoles.map(userBoardRoleMapper::toDto);
        return new PagedModel<>(userBoardRoleDtos);
    }

    //todo
    @Operation(
            summary = "Обновить роль пользователя на доске",
            description = "Изменяет существующую роль пользователя на доске"
    )
    @PatchMapping("/{user-id}/{board-id}")
    @ResponseStatus(HttpStatus.OK)
    public UserBoardRoleDto update(@PathVariable("user-id") Long userId,
                                   @PathVariable("board-id") Long boardId,
                                   @Validated(OnUpdate.class) @RequestBody UserBoardRoleDto userBoardRoleDto) {
        var userBoardRole = userBoardRoleMapper.toEntity(userBoardRoleDto);
        var patched = boardRolesService.patch(userId, boardId, userBoardRole.getBoardRoles());
        return userBoardRoleMapper.toDto(patched);
    }

    @Operation(
            summary = "Удалить конкретную роль пользователя на доске",
            description = "Удаляет указанную роль пользователя с доски"
    )
    @DeleteMapping("/{user-id}/{board-id}/{board-role}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable("user-id") Long userId,
                           @PathVariable("board-id") Long boardId,
                           @PathVariable("board-role") BoardRole boardRole) {
        boardRolesService.deleteBoardRole(userId, boardId, boardRole);
    }
}
