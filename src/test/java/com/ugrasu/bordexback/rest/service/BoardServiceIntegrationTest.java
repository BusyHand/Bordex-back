package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.Role;
import com.ugrasu.bordexback.rest.entity.enums.Scope;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import com.ugrasu.bordexback.rest.repository.UserBoardRoleRepository;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Import(PostgreTestcontainerConfig.class)
public class BoardServiceIntegrationTest {

    @Autowired
    BoardService boardService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserBoardRoleRepository userBoardRoleRepository;

    User testUser;

    @BeforeEach
    public void setUp() {
        userBoardRoleRepository.deleteAll();
        taskRepository.deleteAll();
        userRepository.deleteAll();
        boardRepository.deleteAll();

        // Создаем тестового пользователя перед каждым тестом
        User user = new User();
        user.setUsername("test_user_");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("user" + "@test.com");
        user.setRoles(Set.of(Role.USER, Role.ADMIN));
        testUser = userRepository.save(user);
    }

    @Test
    @DisplayName("Сохранить board")
    public void shouldSaveBoard() {
        Board board = new Board();
        board.setName("Board Name");
        board.setDescription("Board Desc");

        Board saved = boardService.save(board, testUser);

        assertThat(saved.getName()).isEqualTo(board.getName());
        assertThat(saved.getDescription()).isEqualTo(board.getDescription());
        assertThat(saved.getOwner()).isEqualTo(testUser);
    }

    @Test
    @DisplayName("Обновить board")
    public void shouldUpdate() {
        Board board = new Board();
        board.setName("Board");
        board.setDescription("Desc");
        board.setScope(Scope.PUBLIC);

        Board saved = boardService.save(board, testUser);

        Board patch = new Board();
        patch.setName("Updated Name");
        patch.setDescription("Updated Desc");
        patch.setScope(Scope.PRIVATE);

        Board updated = boardService.patch(saved.getId(), patch);

        assertThat(updated.getScope()).isEqualTo(Scope.PRIVATE);
        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.getDescription()).isEqualTo("Updated Desc");
        assertThat(updated.getOwner()).isEqualTo(testUser);
    }

    @Test
    @DisplayName("Сохранить board, затем удалить")
    public void shouldDeleteBoard() {
        Board board = new Board();
        board.setName("To Delete");
        board.setDescription("Desc");

        Board saved = boardService.save(board, testUser);

        Task task1 = new Task();
        task1.setBoard(saved);
        task1.setOwner(testUser);
        task1.setName("Task 1");
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setBoard(saved);
        task2.setOwner(testUser);
        task2.setName("Task 2");
        taskRepository.save(task2);

        boardService.delete(saved.getId());

        assertThatThrownBy(() -> boardService.findOne(saved.getId()))
                .isInstanceOf(EntityNotFoundException.class);
        assertThat(taskRepository.findById(task1.getId())).isNotPresent();
        assertThat(taskRepository.findById(task2.getId())).isNotPresent();
        assertThat(userRepository.findById(testUser.getId())).isPresent();
    }
}
