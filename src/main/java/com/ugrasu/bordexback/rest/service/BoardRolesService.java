package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.rest.controller.filter.BoardRolesFilter;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.BoardRoles;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import com.ugrasu.bordexback.rest.publisher.EventPublisher;
import com.ugrasu.bordexback.rest.repository.BoardRolesRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.ugrasu.bordexback.rest.event.EventType.*;

@Service
@RequiredArgsConstructor
public class BoardRolesService {

    private final BoardRolesRepository boardRolesRepository;
    private final EventPublisher eventPublisher;

    @PreAuthorize(
            """
                    @use.isTheSameUser(#filter.userId()) or @bse.isMember(#filter.boardId())"""
    )
    public Page<BoardRoles> findAll(@P("filter") BoardRolesFilter filter, Pageable pageable) {
        return boardRolesRepository.findAll(filter.filter(), pageable);
    }

    public BoardRoles findOne(Long userId, Long boardId) {
        return boardRolesRepository.findByUser_IdAndBoard_Id(userId, boardId)
                .orElseThrow(() -> new EntityNotFoundException("User board role with user id %s and board id %s not found".formatted(userId, boardId)));
    }

    public BoardRoles save(User owner, Board board, Set<BoardRole> roles) {
        BoardRoles boardRoles = new BoardRoles();
        boardRoles.setBoardRoles(roles);
        owner.addBoardRoles(boardRoles);
        board.addBoardRoles(boardRoles);
        BoardRoles saved = boardRolesRepository.save(boardRoles);
        return eventPublisher.publish(BOARD_ROLE_CREATED, saved);
    }

    @Transactional
    @PreAuthorize(
            """
                    (@bse.isOwner(#boardId)
                    or (@brse.hasBoardRole(#boardId, 'MANAGER') and !@bse.isOwner(#userId, #boardId)
                        and (@brse.allowToUpdateBoardRole(#userId, #boardId, #roles))))"""
    )
    public BoardRoles patch(@P("userId") Long userId, @P("boardId") Long boardId, @P("roles") Set<BoardRole> roles) {
        BoardRoles boardRoles = findOne(userId, boardId);
        boardRoles.setBoardRoles(roles);
        return eventPublisher.publish(BOARD_ROLE_UPDATED, boardRoles);
    }

    @Transactional
    @PreAuthorize(
            """
                    (@bse.isOwner(#boardId)
                    or (@brse.hasBoardRole(#boardId, 'MANAGER')
                        and @brse.allowToDeleteRole(#userId, #boardId, #boardRole)))"""
    )
    public void deleteBoardRole(@P("userId") Long userId, @P("boardId") Long boardId, @P("boardRole") BoardRole boardRole) {
        BoardRoles boardRoles = findOne(userId, boardId);
        boardRoles.getBoardRoles().remove(boardRole);
        eventPublisher.publish(BOARD_ROLE_DELETED, boardRoles);
        if (boardRoles.getBoardRoles().isEmpty()) {
            boardRolesRepository.delete(boardRoles);
        }
    }

    @Transactional
    public void deleteUserRoles(User user, Board board) {
        BoardRoles boardRole = findOne(user.getId(), board.getId());
        user.removeBoardRoles(boardRole);
        board.removeBoardRoles(boardRole);
        eventPublisher.publish(BOARD_ROLES_DELETED, boardRole);
    }
}
