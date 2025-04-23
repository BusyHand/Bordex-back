package com.ugrasu.bordexback.service;

import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.entity.Board;
import com.ugrasu.bordexback.entity.User;
import com.ugrasu.bordexback.entity.UserBoardRole;
import com.ugrasu.bordexback.entity.enums.BoardRole;
import com.ugrasu.bordexback.repository.BoardRepository;
import com.ugrasu.bordexback.repository.TaskRepository;
import com.ugrasu.bordexback.repository.UserBoardRoleRepository;
import com.ugrasu.bordexback.repository.UserRepository;
import com.ugrasu.bordexback.utli.DataGenerator;
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
public class UserBoardRoleIntegrationTest {

    @Autowired
    UserBoardRoleService userBoardRoleService;

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
    @DisplayName("Сохранить userBoardRole")
    public void shouldSaveUserBoardRole() {
        User user = DataGenerator.getSimpleUser();
        user = userRepository.save(user);
        Board board = DataGenerator.getSimpleBoard(user);
        board = boardRepository.save(board);
        UserBoardRole toSave = DataGenerator.getSimpleUserBoardRole(user, board, BoardRole.VIEWER);


        UserBoardRole save = userBoardRoleService.save(user.getId(), board.getId(), toSave);
        UserBoardRole found = userBoardRoleService.findOne(save.getId());


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
        UserBoardRole toSave = DataGenerator.getSimpleUserBoardRole(user, board, BoardRole.VIEWER);
        UserBoardRole toUpdate = DataGenerator.getSimpleUserBoardRole(user, board, BoardRole.VIEWER, BoardRole.DEVELOPER);

        UserBoardRole save = userBoardRoleService.save(user.getId(), board.getId(), toSave);
        userBoardRoleService.patch(user.getId(), board.getId(), toUpdate);
        UserBoardRole found = userBoardRoleService.findOne(save.getId());

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
        UserBoardRole toSave = DataGenerator.getSimpleUserBoardRole(user, board, BoardRole.VIEWER, BoardRole.DEVELOPER, BoardRole.MANAGER);
        Set<BoardRole> outputRoles = Set.of(BoardRole.VIEWER, BoardRole.DEVELOPER);


        UserBoardRole save = userBoardRoleService.save(user.getId(), board.getId(), toSave);
        userBoardRoleService.deleteBoardRole(user.getId(), board.getId(), BoardRole.MANAGER);
        UserBoardRole found = userBoardRoleService.findOne(save.getId());


        assertThat(found.getBoard()).isEqualTo(board);
        assertThat(found.getId()).isEqualTo(save.getId());
        assertThat(found.getBoardRoles()).isEqualTo(outputRoles);
        assertThat(found.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Удалить userBoardRole все роли")
    public void shouldDeleteUserBoardRoleAllRoles() {
        User user = DataGenerator.getSimpleUser();
        user = userRepository.save(user);
        Board board = DataGenerator.getSimpleBoard();
        board.setOwner(user);
        board = boardRepository.save(board);
        UserBoardRole toSave = DataGenerator.getSimpleUserBoardRole(user, board, BoardRole.VIEWER, BoardRole.DEVELOPER, BoardRole.MANAGER);


        UserBoardRole save = userBoardRoleService.save(user.getId(), board.getId(), toSave);
        userBoardRoleService.deleteAll(user.getId(), board.getId());

        assertThatThrownBy(() -> userBoardRoleService.findOne(save.getId()))
                .isInstanceOf(EntityNotFoundException.class);
    }


}
