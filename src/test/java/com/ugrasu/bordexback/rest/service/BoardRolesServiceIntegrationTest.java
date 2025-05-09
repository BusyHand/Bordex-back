package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.BoardRoles;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import com.ugrasu.bordexback.rest.repository.BoardRolesRepository;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import com.ugrasu.bordexback.util.DataGenerator;
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
public class BoardRolesServiceIntegrationTest {

    @Autowired
    BoardRolesService boardRolesService;

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
    @DisplayName("Сохранить userBoardRole")
    public void shouldSaveUserBoardRole() {
        User user = DataGenerator.getSimpleUser();
        user = userRepository.save(user);
        Board board = DataGenerator.getSimpleBoard(user);
        board = boardRepository.save(board);
        DataGenerator.getSimpleUserBoardRole(user, board, BoardRole.VIEWER);


        BoardRoles save = boardRolesService.save(user, board, Set.of(BoardRole.VIEWER));
        BoardRoles found = boardRolesService.findOne(user.getId(), board.getId());


        assertThat(found.getBoard()).isEqualTo(board);
        assertThat(found.getId()).isEqualTo(save.getId());
        assertThat(found.getBoardRoles().size()).isEqualTo(1);
        assertThat(found.getBoardRoles().contains(BoardRole.VIEWER)).isTrue();
        assertThat(found.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Обновить userBoardRole")
    public void shouldPatchUserBoardRole() {
        User user = DataGenerator.getSimpleUser();
        user = userRepository.save(user);
        Board board = DataGenerator.getSimpleBoard(user);
        board = boardRepository.save(board);
        BoardRoles toUpdate = DataGenerator.getSimpleUserBoardRole(user, board, BoardRole.VIEWER, BoardRole.DEVELOPER);

        BoardRoles save = boardRolesService.save(user, board, Set.of(BoardRole.VIEWER));
        boardRolesService.patch(user.getId(), board.getId(), toUpdate.getBoardRoles());
        BoardRoles found = boardRolesService.findOne(user.getId(), board.getId());

        assertThat(found.getBoard()).isEqualTo(board);
        assertThat(found.getId()).isEqualTo(save.getId());
        assertThat(found.getBoardRoles().size()).isEqualTo(2);
        assertThat(found.getBoardRoles().contains(BoardRole.VIEWER)).isTrue();
        assertThat(found.getBoardRoles().contains(BoardRole.DEVELOPER)).isTrue();
        assertThat(found.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Удалить userBoardRole")
    public void shouldDeleteUserBoardRole() {
        User user = DataGenerator.getSimpleUser();
        user = userRepository.save(user);
        Board board = DataGenerator.getSimpleBoard();
        board.setOwner(user);
        board = boardRepository.save(board);
        Set<BoardRole> outputRoles = Set.of(BoardRole.VIEWER, BoardRole.DEVELOPER);

        BoardRoles save = boardRolesService.save(user, board, Set.of(BoardRole.VIEWER, BoardRole.DEVELOPER, BoardRole.MANAGER));
        boardRolesService.deleteBoardRole(user.getId(), board.getId(), BoardRole.MANAGER);
        BoardRoles found = boardRolesService.findOne(user.getId(), board.getId());

        assertThat(found.getBoard()).isEqualTo(board);
        assertThat(found.getId()).isEqualTo(save.getId());
        assertThat(found.getBoardRoles()).isEqualTo(outputRoles);
        assertThat(found.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Удалить userBoardRole все роли")
    public void shouldDeleteUserBoardRoleAllRoles() {
        User user = DataGenerator.getSimpleUser();
        User savedUser = userRepository.save(user);
        Board board = DataGenerator.getSimpleBoard();
        board.setOwner(user);
        Board savedBoard = boardRepository.save(board);

        boardRolesService.save(user, board, Set.of(BoardRole.VIEWER, BoardRole.DEVELOPER, BoardRole.MANAGER));
        boardRolesService.deleteUserRoles(user, board);

        assertThatThrownBy(() -> boardRolesService.findOne(savedUser.getId(), savedBoard.getId()))
                .isInstanceOf(EntityNotFoundException.class);
    }


}
