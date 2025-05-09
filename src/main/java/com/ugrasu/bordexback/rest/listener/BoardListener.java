package com.ugrasu.bordexback.rest.listener;

import com.ugrasu.bordexback.rest.dto.payload.BoardPayload;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.event.EventType;
import com.ugrasu.bordexback.rest.event.TaskEvent;
import com.ugrasu.bordexback.rest.publisher.EventPublisher;
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
public class BoardListener {

    private final BoardService boardService;
    private final EventPublisher eventPublisher;

    @EventListener
    @Transactional
    public void listenTaskEvent(TaskEvent taskEvent) {
        EventType eventType = taskEvent.getTaskPayload().getEventType();
        BoardPayload board = taskEvent.getTaskPayload().getBoard();
        if (TASK_CREATED.equals(eventType)) {
            listenTaskCreated(board);
        }
        if (TASK_DELETED.equals(eventType)) {
            listenTaskDelete(board);
        }
    }

    public void listenTaskCreated(BoardPayload boardPayload) {
        Board board = boardService.findOne(boardPayload.getId());
        eventPublisher.publish(BOARD_UPDATED, board);
    }

    public void listenTaskDelete(BoardPayload boardPayload) {
        Board board = boardService.findOne(boardPayload.getId());
        eventPublisher.publish(BOARD_UPDATED, board);
    }
}
