package com.ugrasu.bordexback.rest.controller;


import com.ugrasu.bordexback.rest.controller.filter.UserBoardRoleFilter;
import com.ugrasu.bordexback.rest.dto.full.UserBoardRoleDto;
import com.ugrasu.bordexback.rest.dto.validation.OnCreate;
import com.ugrasu.bordexback.rest.dto.validation.OnUpdate;
import com.ugrasu.bordexback.rest.entity.UserBoardRole;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import com.ugrasu.bordexback.rest.mapper.impl.UserBoardRoleMapper;
import com.ugrasu.bordexback.rest.service.UserBoardRoleService;
import com.ugrasu.bordexback.websocket.BoardWebSocketService;
import io.swagger.v3.oas.annotations.Operation;
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
@RequestMapping("/api/users/boards/roles")
@RequiredArgsConstructor
public class UserBoardRoleController {

    private final UserBoardRoleService userBoardRoleService;
    private final UserBoardRoleMapper userBoardRoleMapper;

    //todo dev purpose
    private final BoardWebSocketService boardWebSocketService;

    @GetMapping
    @Operation(summary = "Получить список ролей пользователей на досках", description = "Возвращает постраничный список ролей с фильтрацией по userId, boardId, и boardRole")
    public PagedModel<UserBoardRoleDto> findAll(@ParameterObject @ModelAttribute UserBoardRoleFilter filter,
                                                @ParameterObject Pageable pageable) {
        Specification<UserBoardRole> specification = filter.toSpecification();
        Page<UserBoardRole> userBoardRoles = userBoardRoleService.findAll(specification, pageable);
        Page<UserBoardRoleDto> userBoardRoleDtos = userBoardRoles.map(userBoardRoleMapper::toDto);
        return new PagedModel<>(userBoardRoleDtos);
    }

    @PostMapping("/{user-id}/{board-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserBoardRoleDto save(@PathVariable("user-id") Long userId,
                                 @PathVariable("board-id") Long boardId,
                                 @Validated(OnCreate.class) @RequestBody UserBoardRoleDto userBoardRoleDto) {
        var userBoardRole = userBoardRoleMapper.toEntity(userBoardRoleDto);
        var saved = userBoardRoleService.save(userId, boardId, userBoardRole);

        var dto = userBoardRoleMapper.toDto(saved);
        boardWebSocketService.sendBoardRoleUpdate(dto);
        return dto;
    }

    @PatchMapping("/{user-id}/{board-id}")
    @ResponseStatus(HttpStatus.OK)
    public UserBoardRoleDto update(@PathVariable("user-id") Long userId,
                                   @PathVariable("board-id") Long boardId,
                                   @Validated(OnUpdate.class) @RequestBody UserBoardRoleDto userBoardRoleDto) {
        var userBoardRole = userBoardRoleMapper.toEntity(userBoardRoleDto);
        var patched = userBoardRoleService.patch(userId, boardId, userBoardRole);

        var dto = userBoardRoleMapper.toDto(patched);
        boardWebSocketService.sendBoardRoleUpdate(dto);
        return dto;
    }

    @DeleteMapping("/{user-id}/{board-id}/{board-role}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable("user-id") Long userId,
                           @PathVariable("board-id") Long boardId,
                           @PathVariable("board-role") BoardRole boardRole) {
        UserBoardRole one = userBoardRoleService.findOne(userId, boardId);
        userBoardRoleService.deleteBoardRole(userId, boardId, boardRole);
        boardWebSocketService.sendBoardRoleDelete(userBoardRoleMapper.toDto(one));
    }

    @DeleteMapping("/{user-id}/{board-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("user-id") Long userId,
                       @PathVariable("board-id") Long boardId) {
        UserBoardRole one = userBoardRoleService.findOne(userId, boardId);
        userBoardRoleService.deleteAll(userId, boardId);
        boardWebSocketService.sendBoardRoleDelete(userBoardRoleMapper.toDto(one));
    }
}
