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
import com.ugrasu.bordexback.utli.BoardDataUtil;
import com.ugrasu.bordexback.utli.UserDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

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
        User user = UserDataUtil.getSimpleUser();
        user = userRepository.save(user);
        Board board = BoardDataUtil.getSimpleBoard();
        board.setOwner(user);
        board = boardRepository.save(board);
        Set<BoardRole> boardRoles = Set.of(BoardRole.EMPLOYEE);


        UserBoardRole save = userBoardRoleService.save(user.getId(), board.getId(), boardRoles);
        UserBoardRole found = userBoardRoleService.findOne(save.getId());

        assertThat(found.getBoard()).isEqualTo(board);
        assertThat(found.getId()).isEqualTo(save.getId());
        assertThat(found.getBoardRoles()).isEqualTo(boardRoles);
        assertThat(found.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Обновить userBoardRole")
    public void shouldPatchUserBoardRole() {
        User user = UserDataUtil.getSimpleUser();
        user = userRepository.save(user);
        Board board = BoardDataUtil.getSimpleBoard();
        board.setOwner(user);
        board = boardRepository.save(board);
        Set<BoardRole> boardRoles = Set.of(BoardRole.EMPLOYEE);
        Set<BoardRole> newBoardRoles = Set.of(BoardRole.EMPLOYEE, BoardRole.DEVELOPER);


        UserBoardRole save = userBoardRoleService.save(user.getId(), board.getId(), boardRoles);
        userBoardRoleService.patch(user.getId(), board.getId(), newBoardRoles);
        UserBoardRole found = userBoardRoleService.findOne(save.getId());

        assertThat(found.getBoard()).isEqualTo(board);
        assertThat(found.getId()).isEqualTo(save.getId());
        assertThat(found.getBoardRoles()).isEqualTo(newBoardRoles);
        assertThat(found.getUser()).isEqualTo(user);
    }

    @Test
    @DisplayName("Удалить userBoardRole")
    public void shouldDeleteUserBoardRole() {
        User user = UserDataUtil.getSimpleUser();
        user = userRepository.save(user);
        Board board = BoardDataUtil.getSimpleBoard();
        board.setOwner(user);
        board = boardRepository.save(board);
        Set<BoardRole> boardRoles = Set.of(BoardRole.EMPLOYEE, BoardRole.DEVELOPER, BoardRole.MANAGER);
        Set<BoardRole> outputRoles = Set.of(BoardRole.EMPLOYEE, BoardRole.DEVELOPER);


        UserBoardRole save = userBoardRoleService.save(user.getId(), board.getId(), boardRoles);
        userBoardRoleService.delete(user.getId(), board.getId(), Set.of(BoardRole.MANAGER));
        UserBoardRole found = userBoardRoleService.findOne(save.getId());

        assertThat(found.getBoard()).isEqualTo(board);
        assertThat(found.getId()).isEqualTo(save.getId());
        assertThat(found.getBoardRoles()).isEqualTo(outputRoles);
        assertThat(found.getUser()).isEqualTo(user);
    }


}
