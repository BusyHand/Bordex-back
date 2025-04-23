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

    public UserBoardRole save(Long boardId, Long userId, UserBoardRole newUserBoardRole) {
        UserBoardRole userBoardRole = new UserBoardRole();
        userBoardRole.setBoard(boardService.findOne(boardId));
        userBoardRole.setUser(userService.findOne(userId));
        userBoardRole.setBoardRoles(newUserBoardRole.getBoardRoles());
        return userBoardRoleRepository.save(userBoardRole);
    }

    public UserBoardRole patch(Long boardId, Long userId, UserBoardRole newUserBoardRole) {
        UserBoardRole userBoardRole = userBoardRoleRepository.findByUser_IdAndBoard_Id(userId, boardId)
                .orElseThrow(() -> new EntityNotFoundException("User board role with user id %s not found".formatted(userId)));
        userBoardRole.setBoardRoles(newUserBoardRole.getBoardRoles());
        return userBoardRoleRepository.save(userBoardRole);
    }

    @Transactional
    public void deleteBoardRole(Long userId, Long boardId, BoardRole boardRole) {
        UserBoardRole userBoardRole = userBoardRoleRepository.findByUser_IdAndBoard_Id(userId, boardId)
                .orElseThrow(() -> new EntityNotFoundException("UserBoardRole not found"));
        userBoardRole.getBoardRoles().remove(boardRole);
        if (userBoardRole.getBoardRoles().isEmpty()) {
            userBoardRoleRepository.delete(userBoardRole);
        } else {
            userBoardRoleRepository.save(userBoardRole);
        }
    }


    public void deleteAll(Long userId, Long boardId) {
        userBoardRoleRepository.deleteByUser_IdAndBoard_Id(userId, boardId);
    }
}
