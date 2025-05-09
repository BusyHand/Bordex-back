package com.ugrasu.bordexback.rest.facade;

import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import com.ugrasu.bordexback.rest.repository.BoardRolesRepository;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import com.ugrasu.bordexback.util.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(PostgreTestcontainerConfig.class)
public class TaskFacadeManagementIntegrationTest {

    @Autowired
    TaskFacadeManagement taskFacadeManagement;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    BoardRolesRepository boardRolesRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        boardRepository.deleteAll();
        taskRepository.deleteAll();
        boardRolesRepository.deleteAll();
    }

    @Test
    @DisplayName("Должно создать задачу")
    public void shouldCreateTask() {
        User owner = DataGenerator.getSimpleUser();
        owner = userRepository.save(owner);
        Board board = DataGenerator.getSimpleBoard();
        board.setOwner(owner);
        board = boardRepository.save(board);
        Task task = DataGenerator.getSimpleTask();

        Task created = taskFacadeManagement.createTask(board.getId(), owner.getId(), task);

        Task saved = taskRepository.findById(created.getId()).orElseThrow();
        assertThat(saved.getName()).isEqualTo(created.getName());
        assertThat(saved.getDescription()).isEqualTo(created.getDescription());
        assertThat(saved.getOwner()).isEqualTo(owner);
        assertThat(saved.getBoard()).isEqualTo(board);
        assertThat(saved.getStatus()).isEqualTo(created.getStatus());
        assertThat(saved.getPriority()).isEqualTo(created.getPriority());
    }

    @Test
    @DisplayName("Должно назначить пользователя на задачу")
    public void shouldAssignUserToTask() {
        User owner = DataGenerator.getSimpleUser();
        owner = userRepository.save(owner);
        Board board = DataGenerator.getSimpleBoard();
        board.setOwner(owner);
        board = boardRepository.save(board);
        Task task = DataGenerator.getSimpleTask();
        task.setOwner(owner);
        task.setBoard(board);
        task = taskRepository.save(task);

        Task saved = taskFacadeManagement.assignUserToTask(task.getId(), owner.getId());

        assertThat(saved.getAssignees().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("Должно снять пользователя с задачи")
    public void shouldUnassignUserToTask() {
        User owner = DataGenerator.getSimpleUser();
        owner = userRepository.save(owner);
        Board board = DataGenerator.getSimpleBoard();
        board.setOwner(owner);
        board = boardRepository.save(board);
        Task task = DataGenerator.getSimpleTask();
        task.setOwner(owner);
        task.setBoard(board);
        task.addAssignee(owner);
        task = taskRepository.save(task);

        Task saved = taskFacadeManagement.unassignUserFromTask(task.getId(), owner.getId());

        assertThat(saved.getAssignees().size()).isEqualTo(0);
    }
}
