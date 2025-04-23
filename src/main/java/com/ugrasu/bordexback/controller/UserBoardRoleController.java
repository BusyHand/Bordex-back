package com.ugrasu.bordexback.controller;


import com.ugrasu.bordexback.dto.full.UserBoardRoleDto;
import com.ugrasu.bordexback.entity.UserBoardRole;
import com.ugrasu.bordexback.entity.enums.BoardRole;
import com.ugrasu.bordexback.mapper.impl.UserBoardRoleMapper;
import com.ugrasu.bordexback.service.UserBoardRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/boards/roles")
@RequiredArgsConstructor
public class UserBoardRoleController {

    private final UserBoardRoleService userBoardRoleService;
    private final UserBoardRoleMapper userBoardRoleMapper;

    @GetMapping
    public PagedModel<UserBoardRoleDto> findAll(Pageable pageable) {
        Page<UserBoardRole> boards = userBoardRoleService.findAll(pageable);
        Page<UserBoardRoleDto> boardDtos = boards.map(userBoardRoleMapper::toDto);
        return new PagedModel<>(boardDtos);
    }

    @GetMapping("/{id}")
    public UserBoardRoleDto findById(@PathVariable Long id) {
        UserBoardRole userBoardRole = userBoardRoleService.findOne(id);
        return userBoardRoleMapper.toDto(userBoardRole);
    }

    @PostMapping("/{user-id}/{board-id}")
    @ResponseStatus(HttpStatus.CREATED)
    public UserBoardRoleDto save(@PathVariable("user-id") Long userId,
                                 @PathVariable("board-id") Long boardId,
                                 @RequestBody UserBoardRoleDto userBoardRoleDto) {
        UserBoardRole userBoardRole = userBoardRoleMapper.toEntity(userBoardRoleDto);
        userBoardRole = userBoardRoleService.save(userId, boardId, userBoardRole);
        return userBoardRoleMapper.toDto(userBoardRole);
    }

    @PatchMapping("/{user-id}/{board-id}")
    @ResponseStatus(HttpStatus.OK)
    public UserBoardRoleDto update(@PathVariable("user-id") Long userId,
                                   @PathVariable("board-id") Long boardId,
                                   @RequestBody UserBoardRoleDto userBoardRoleDto) {
        UserBoardRole userBoardRole = userBoardRoleMapper.toEntity(userBoardRoleDto);
        UserBoardRole patched = userBoardRoleService.patch(userId, boardId, userBoardRole);
        return userBoardRoleMapper.toDto(patched);
    }

    @DeleteMapping("/{user-id}/{board-id}/{board-role}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRole(@PathVariable("user-id") Long userId,
                           @PathVariable("board-id") Long boardId,
                           @PathVariable("board-role") BoardRole boardRole) {
        userBoardRoleService.deleteBoardRole(userId, boardId, boardRole);
    }

    @DeleteMapping("/{user-id}/{board-id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("user-id") Long userId,
                       @PathVariable("board-id") Long boardId) {
        userBoardRoleService.deleteAll(userId, boardId);
    }

}
