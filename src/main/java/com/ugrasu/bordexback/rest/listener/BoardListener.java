package com.ugrasu.bordexback.rest.listener;

import com.ugrasu.bordexback.rest.dto.payload.BoardPayload;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.event.EventType;
import com.ugrasu.bordexback.rest.event.TaskEvent;
import com.ugrasu.bordexback.rest.publisher.EventPublisher;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.ugrasu.bordexback.rest.event.EventType.*;

@Async
@Component
@RequiredArgsConstructor
public class BoardListener {

    private final BoardRepository boardRepository;
    private final EventPublisher eventPublisher;
    private final TaskRepository taskRepository;

    @EventListener
    @Transactional
    public void listenTaskEvent(TaskEvent taskEvent) {
        EventType eventType = taskEvent.getTaskPayload().getEventType();
        BoardPayload board = taskEvent.getTaskPayload().getBoard();
        if (TASK_CREATED.equals(eventType) || TASK_DELETED.equals(eventType) || TASK_UPDATED.equals(eventType)) {
            listenTaskUpdate(board);
        }
    }

    private void listenTaskUpdate(BoardPayload boardPayload) {
        Board board = boardRepository.findById(boardPayload.getId()).get();
        calculateAverageProgressByBoard(board);
        eventPublisher.publish(BOARD_UPDATED, board);
    }

    public void calculateAverageProgressByBoard(Board board) {
        int boardProgress = taskRepository.calculateAverageProgressByBoard(board);
        board.setProgress(boardProgress);
        boardRepository.save(board);
    }
}
