package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.Priority;
import com.ugrasu.bordexback.rest.entity.enums.Status;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import com.ugrasu.bordexback.rest.repository.UserBoardRoleRepository;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import com.ugrasu.bordexback.util.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Import(PostgreTestcontainerConfig.class)
public class TaskServiceIntegrationTest {

    @Autowired
    TaskService taskService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserBoardRoleRepository userBoardRoleRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        boardRepository.deleteAll();
        taskRepository.deleteAll();
        userBoardRoleRepository.deleteAll();
    }

    @Test
    @DisplayName("Сохранить task")
    public void shouldSaveUser() {
        User owner = DataGenerator.getSimpleUser();
        owner = userRepository.save(owner);
        Board board = DataGenerator.getSimpleBoard();
        board.setOwner(owner);
        board = boardRepository.save(board);
        Task task = DataGenerator.getSimpleTask();

        Task saved = taskService.save(board.getId(), task, owner);

        assertThat(saved.getName()).isEqualTo(task.getName());
        assertThat(saved.getDescription()).isEqualTo(task.getDescription());
        assertThat(saved.getBoard()).isEqualTo(board);
        assertThat(saved.getOwner()).isEqualTo(owner);

    }

    @Test
    @DisplayName("Обновить task")
    public void shouldUpdateTask() {
        User owner = DataGenerator.getSimpleUser();
        owner = userRepository.save(owner);
        Board board = DataGenerator.getSimpleBoard();
        board.setOwner(owner);
        board = boardRepository.save(board);
        Task task = DataGenerator.getSimpleTask();
        Task taskToUpdate = DataGenerator.getSimpleTask();
        taskToUpdate.setName("new Name");
        taskToUpdate.setDescription("new Description");
        taskToUpdate.setPriority(Priority.LOW);
        taskToUpdate.setStatus(Status.DONE);
        taskToUpdate.setDeadline(LocalDateTime.now().plusDays(4));


        Task saved = taskService.save(board.getId(), task, owner);
        Task patched = taskService.patch(task.getId(), taskToUpdate);

        assertThat(patched.getId()).isEqualTo(saved.getId());
        assertThat(patched.getName()).isEqualTo(taskToUpdate.getName());
        assertThat(patched.getDescription()).isEqualTo(taskToUpdate.getDescription());
        assertThat(patched.getPriority()).isEqualTo(taskToUpdate.getPriority());
        assertThat(patched.getStatus()).isEqualTo(taskToUpdate.getStatus());
        assertThat(patched.getDeadline()).isEqualTo(taskToUpdate.getDeadline());
    }

    @Test
    @DisplayName("Удалить task")
    public void shouldDeleteTask() {
        User owner = DataGenerator.getSimpleUser();
        owner = userRepository.save(owner);
        Board board = DataGenerator.getSimpleBoard();
        board.setOwner(owner);
        board = boardRepository.save(board);
        Task task = DataGenerator.getSimpleTask();
        Task saved = taskService.save(board.getId(), task, owner);

        taskService.delete(saved.getId());

        assertThat(taskRepository.findById(saved.getId()).isPresent()).isFalse();
        assertThat(userRepository.findById(owner.getId()).isPresent()).isTrue();
        assertThat(boardRepository.findById(board.getId()).isPresent()).isTrue();
    }

}
