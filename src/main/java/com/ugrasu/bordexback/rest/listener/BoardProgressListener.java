package com.ugrasu.bordexback.rest.listener;

import com.ugrasu.bordexback.rest.dto.payload.TaskPayload;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.event.EventType;
import com.ugrasu.bordexback.rest.event.TaskEvent;
import com.ugrasu.bordexback.rest.publisher.EventPublisher;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import com.ugrasu.bordexback.rest.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.ugrasu.bordexback.rest.event.EventType.*;

@Async
@Component
@RequiredArgsConstructor
public class BoardProgressListener {

    private final TaskRepository taskRepository;
    private final BoardRepository boardRepository;
    private final BoardService boardService;
    private final EventPublisher eventPublisher;

    @EventListener
    @Transactional
    public void listenTaskEvent(TaskEvent taskEvent) {
        TaskPayload taskPayload = taskEvent.getTaskPayload();
        EventType eventType = taskPayload.getEventType();
        if (TASK_CREATED.equals(eventType) || TASK_DELETED.equals(eventType) || TASK_UPDATED.equals(eventType)) {
            Long boardId = taskPayload.getBoard()
                    .getId();
            Board board = boardService.findOne(boardId);
            int boardProgress = taskRepository.calculateAverageProgressByBoard(board);
            board.setProgress(boardProgress);
            boardRepository.save(board);
            eventPublisher.publish(BOARD_UPDATED, board);
        }
    }
}
