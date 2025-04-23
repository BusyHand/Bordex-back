package com.ugrasu.bordexback.service;

import com.ugrasu.bordexback.entity.Board;
import com.ugrasu.bordexback.entity.User;
import com.ugrasu.bordexback.entity.UserBoardRole;
import com.ugrasu.bordexback.entity.enums.BoardRole;
import com.ugrasu.bordexback.mapper.impl.BoardMapper;
import com.ugrasu.bordexback.repository.BoardRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final UserBoardRoleService userBoardRoleService;
    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;

    public Page<Board> findAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    public Board findOne(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Board with id %s not found".formatted(id)));
    }

    public Board save(Board board, User owner) {
        board.setId(null);
        board.setOwner(owner);
        board = boardRepository.save(board);
        UserBoardRole userBoardRole = userBoardRoleService.save(owner.getId(), board.getId(), Set.of(BoardRole.MANAGER, BoardRole.EMPLOYEE, BoardRole.DEVELOPER));
        board.setUserBoardRoles(Set.of(userBoardRole));
        return boardRepository.save(board);
    }

    public Board patch(Long oldBoardId, Board newBoard) {
        Board oldBoard = findOne(oldBoardId);
        Board updatedBoard = boardMapper.partialUpdate(newBoard, oldBoard);
        return boardRepository.save(updatedBoard);
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }


}
