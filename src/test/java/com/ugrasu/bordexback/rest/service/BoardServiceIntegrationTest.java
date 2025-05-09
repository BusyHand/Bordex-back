package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.Role;
import com.ugrasu.bordexback.rest.entity.enums.Scope;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import com.ugrasu.bordexback.rest.repository.BoardRolesRepository;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
    BoardRolesRepository boardRolesRepository;
    @Autowired
    EntityManager entityManager;


    @BeforeEach
    public void setUp() {
        boardRolesRepository.deleteAll();
        taskRepository.deleteAll();
        userRepository.deleteAll();
        boardRepository.deleteAll();
    }

    @Test
    @DisplayName("Сохранить board")
    @Transactional
    public void shouldSaveBoard() {
        Board board = new Board();
        board.setName("Board Name");
        board.setDescription("Board Desc");
        User user = new User();
        user.setUsername("test_user_");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("user" + "@test.com");
        user.setRoles(Set.of(Role.USER, Role.ADMIN));
        User testUser = userRepository.save(user);

        Board saved = boardService.save(board, testUser);

        saved = boardRepository.findById(saved.getId()).get();
        assertThat(saved.getName()).isEqualTo(board.getName());
        assertThat(saved.getDescription()).isEqualTo(board.getDescription());
        assertThat(saved.getOwner()).isEqualTo(testUser);
    }

    @Test
    @DisplayName("Обновить board")
    @Transactional
    public void shouldUpdate() {
        Board board = new Board();
        board.setName("Board");
        board.setDescription("Desc");
        board.setScope(Scope.PUBLIC);
        User user = new User();
        user.setUsername("test_user_");
        user.setFirstName("First");
        user.setLastName("Last");
        user.setEmail("user" + "@test.com");
        user.setRoles(Set.of(Role.USER, Role.ADMIN));
        User testUser = userRepository.save(user);

        Board saved = boardService.save(board, testUser);

        Board patch = new Board();
        patch.setName("Updated Name");
        patch.setDescription("Updated Desc");
        patch.setScope(Scope.PRIVATE);

        Board updated = boardService.patch(saved.getId(), patch);

        updated = boardRepository.findById(saved.getId()).get();
        assertThat(updated.getScope()).isEqualTo(Scope.PRIVATE);
        assertThat(updated.getName()).isEqualTo("Updated Name");
        assertThat(updated.getDescription()).isEqualTo("Updated Desc");
        assertThat(updated.getOwner()).isEqualTo(testUser);
    }

}
