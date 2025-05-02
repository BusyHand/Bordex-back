package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.rest.entity.UserBoardRole;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import com.ugrasu.bordexback.rest.publisher.EventPublisher;
import com.ugrasu.bordexback.rest.repository.UserBoardRoleRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ugrasu.bordexback.rest.event.EventType.*;

@Service
@RequiredArgsConstructor
public class UserBoardRoleService {

    private final UserBoardRoleRepository userBoardRoleRepository;
    private final EventPublisher eventPublisher;
    private final UserService userService;
    private final BoardService boardService;

    public Page<UserBoardRole> findAll(Specification<UserBoardRole> specification, Pageable pageable) {
        return userBoardRoleRepository.findAll(specification, pageable);
    }

    public UserBoardRole findOne(Long userId, Long boardId) {
        return userBoardRoleRepository.findByUser_IdAndBoard_Id(userId, boardId)
                .orElseThrow(() -> new EntityNotFoundException("User board role with user id %s and board id %s not found".formatted(userId, boardId)));
    }

    public UserBoardRole save(Long boardId, Long userId, UserBoardRole newUserBoardRole) {
        UserBoardRole userBoardRole = new UserBoardRole();
        userBoardRole.setBoard(boardService.findOne(boardId));
        userBoardRole.setUser(userService.findOne(userId));
        userBoardRole.setBoardRoles(newUserBoardRole.getBoardRoles());
        return eventPublisher.publish(BOARD_ROLE_CREATED,
                userBoardRoleRepository.save(userBoardRole));
    }

    @Transactional
    public UserBoardRole patch(Long boardId, Long userId, UserBoardRole newUserBoardRole) {
        UserBoardRole userBoardRole = findOne(boardId, userId);
        userBoardRole.setBoardRoles(newUserBoardRole.getBoardRoles());
        return eventPublisher.publish(BOARD_ROLE_UPDATED,
                userBoardRoleRepository.save(userBoardRole));
    }

    @Transactional
    public void deleteBoardRole(Long userId, Long boardId, BoardRole boardRole) {
        UserBoardRole userBoardRole = findOne(userId, boardId);
        userBoardRole.getBoardRoles().remove(boardRole);
        if (userBoardRole.getBoardRoles().isEmpty()) {
            userBoardRoleRepository.delete(userBoardRole);
        } else {
            userBoardRoleRepository.save(userBoardRole);
        }
        eventPublisher.publish(BOARD_ROLE_DELETED, userBoardRole);
    }

    @Transactional
    public void deleteAll(Long userId, Long boardId) {
        UserBoardRole boardRole = findOne(userId, boardId);
        userBoardRoleRepository.deleteByUser_IdAndBoard_Id(userId, boardId);
        eventPublisher.publish(BOARD_ROLE_DELETED, boardRole);
    }
}
