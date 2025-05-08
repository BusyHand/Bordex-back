package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.BoardRoles;
import com.ugrasu.bordexback.rest.entity.User;
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

import java.util.Set;

import static com.ugrasu.bordexback.rest.event.EventType.*;

@Service
@RequiredArgsConstructor
public class BoardRolesService {

    private final UserBoardRoleRepository userBoardRoleRepository;
    private final EventPublisher eventPublisher;

    public Page<BoardRoles> findAll(Specification<BoardRoles> specification, Pageable pageable) {
        return userBoardRoleRepository.findAll(specification, pageable);
    }

    public BoardRoles findOne(Long userId, Long boardId) {
        return userBoardRoleRepository.findByUser_IdAndBoard_Id(userId, boardId)
                .orElseThrow(() -> new EntityNotFoundException("User board role with user id %s and board id %s not found".formatted(userId, boardId)));
    }

    public BoardRoles save(User owner, Board board, Set<BoardRole> roles) {
        BoardRoles boardRoles = new BoardRoles();
        boardRoles.setBoardRoles(roles);
        owner.addBoardRoles(boardRoles);
        board.addBoardRoles(boardRoles);
        BoardRoles saved = userBoardRoleRepository.save(boardRoles);
        return eventPublisher.publish(BOARD_ROLE_CREATED, saved);
    }

    @Transactional
    public BoardRoles patch(Long userId, Long boardId, Set<BoardRole> roles) {
        BoardRoles boardRoles = findOne(userId, boardId);
        boardRoles.setBoardRoles(roles);
        return eventPublisher.publish(BOARD_ROLE_UPDATED, boardRoles);
    }

    @Transactional
    public void deleteBoardRole(Long userId, Long boardId, BoardRole boardRole) {
        BoardRoles boardRoles = findOne(userId, boardId);
        boardRoles.getBoardRoles().remove(boardRole);
        if (boardRoles.getBoardRoles().isEmpty()) {
            userBoardRoleRepository.delete(boardRoles);
        } else {
            userBoardRoleRepository.save(boardRoles);
        }
        eventPublisher.publish(BOARD_ROLE_DELETED, boardRoles);
    }

    //todo publish alraydy deleted
    @Transactional
    public void deleteUserRoles(User user, Board board) {
        BoardRoles boardRole = findOne(user.getId(), board.getId());
        user.removeBoardRoles(boardRole);
        board.removeBoardRoles(boardRole);
        eventPublisher.publish(BOARD_ROLES_DELETED, boardRole);
    }
}
