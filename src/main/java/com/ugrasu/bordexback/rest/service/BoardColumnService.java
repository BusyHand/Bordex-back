package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.rest.entity.BoardColumn;
import com.ugrasu.bordexback.rest.mapper.impl.BoardColumnMapper;
import com.ugrasu.bordexback.rest.publisher.EventPublisher;
import com.ugrasu.bordexback.rest.repository.BoardColumnRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ugrasu.bordexback.rest.event.EventType.BOARD_COLUMN_ADD;
import static com.ugrasu.bordexback.rest.event.EventType.BOARD_COLUMN_UPDATE;

@Service
@RequiredArgsConstructor
public class BoardColumnService {

    private final BoardColumnRepository boardColumnRepository;
    private final BoardColumnMapper boardColumnMapper;
    private final EventPublisher eventPublisher;

    public BoardColumn find(Long columnId) {
        return boardColumnRepository.findById(columnId)
                .orElseThrow(() -> new EntityNotFoundException("Board Column Not Found"));
    }

    public void save(BoardColumn boardColumn) {
        BoardColumn saved = boardColumnRepository.save(boardColumn);
        eventPublisher.publish(BOARD_COLUMN_ADD, saved.getBoard());
    }

    @Transactional
    public void delete(Long columnId) {
        boardColumnRepository.deleteById(columnId);
    }

    public BoardColumn patch(Long columnId, BoardColumn updatedBoardColumn) {
        BoardColumn boardColumn = find(columnId);
        boardColumnMapper.partialUpdate(updatedBoardColumn, boardColumn);
        boardColumnRepository.save(boardColumn);
        eventPublisher.publish(BOARD_COLUMN_UPDATE, boardColumn.getBoard());
        return boardColumn;
    }
}
