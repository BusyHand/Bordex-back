package com.ugrasu.bordexback.service;

import com.ugrasu.bordexback.entity.UserBoardRole;
import com.ugrasu.bordexback.entity.enums.BoardRole;
import com.ugrasu.bordexback.repository.UserBoardRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserBoardRoleService {

    private final UserBoardRoleRepository userBoardRoleRepository;
    private final UserService userService;
    private final BoardService boardService;

    public Page<UserBoardRole> findAll(Pageable pageable) {
        return userBoardRoleRepository.findAll(pageable);
    }

    public UserBoardRole findOne(Long id) {
        return userBoardRoleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User board role with id %s not found".formatted(id)));
    }

    public UserBoardRole save(Long boardId, Long userId, Set<BoardRole> boardRoles) {
        UserBoardRole userBoardRole = new UserBoardRole();
        userBoardRole.setBoard(boardService.findOne(boardId));
        userBoardRole.setUser(userService.findOne(userId));
        userBoardRole.setBoardRoles(boardRoles);
        return userBoardRoleRepository.save(userBoardRole);
    }

    public UserBoardRole patch(Long boardId, Long userId, Set<BoardRole> boardRoles) {
        UserBoardRole userBoardRole = userBoardRoleRepository.findByUser_IdAndBoard_Id(userId, boardId)
                .orElseThrow(() -> new EntityNotFoundException("User board role with user id %s not found".formatted(userId)));
        userBoardRole.setBoardRoles(boardRoles);
        return userBoardRoleRepository.save(userBoardRole);
    }

    @Transactional
    public void delete(Long boardId, Long userId, Set<BoardRole> boardRolesToRemove) {
        UserBoardRole userBoardRole = userBoardRoleRepository.findByUser_IdAndBoard_Id(userId, boardId)
                .orElseThrow(() -> new EntityNotFoundException("User board role with user id %s not found".formatted(userId)));
        userBoardRole.getBoardRoles().removeAll(boardRolesToRemove);
        userBoardRoleRepository.save(userBoardRole);
    }


}
