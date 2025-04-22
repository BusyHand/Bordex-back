package com.ugrasu.bordexback.service;

import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.entity.Board;
import com.ugrasu.bordexback.entity.Task;
import com.ugrasu.bordexback.entity.User;
import com.ugrasu.bordexback.entity.enums.Role;
import com.ugrasu.bordexback.repository.BoardRepository;
import com.ugrasu.bordexback.repository.TaskRepository;
import com.ugrasu.bordexback.repository.UserBoardRoleRepository;
import com.ugrasu.bordexback.repository.UserRepository;
import com.ugrasu.bordexback.utli.BoardDataUtil;
import com.ugrasu.bordexback.utli.TaskDataUtil;
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
public class UserServiceIntegrationTest {

    @Autowired
    UserService userService;

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
    @DisplayName("Сохранить user")
    public void shouldSaveUser() {
        User user = UserDataUtil.getSimpleUser();

        User save = userService.save(user);

        assertThat(save.getEmail()).isEqualTo(user.getEmail());
        assertThat(save.getPassword()).isEqualTo(user.getPassword());
        assertThat(save.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(save.getLastName()).isEqualTo(user.getLastName());
        assertThat(save.getUsername()).isEqualTo(user.getUsername());
    }

    @Test
    @DisplayName("Обновить user")
    public void shouldPatchUser() {
        User user = UserDataUtil.getSimpleUser();
        String newEmail = "New email";
        String newPassword = "New password";
        String newFirstName = "New first name";
        String newLastName = "New last name";
        String newUsername = "New username";
        Set<Role> newRoles = Set.of(Role.USER, Role.ADMIN);


        User save = userService.save(user);
        User userToPatch = UserDataUtil.getSimpleUser();
        userToPatch.setEmail(newEmail);
        userToPatch.setPassword(newPassword);
        userToPatch.setFirstName(newFirstName);
        userToPatch.setLastName(newLastName);
        userToPatch.setUsername(newUsername);
        userToPatch.setRoles(newRoles);
        User patched = userService.patch(save.getId(), userToPatch);


        assertThat(patched.getId()).isEqualTo(save.getId());
        assertThat(patched.getEmail()).isEqualTo(newEmail);
        assertThat(patched.getPassword()).isEqualTo(newPassword);
        assertThat(patched.getFirstName()).isEqualTo(newFirstName);
        assertThat(patched.getLastName()).isEqualTo(newLastName);
        assertThat(patched.getUsername()).isEqualTo(newUsername);
        assertThat(patched.getRoles()).isEqualTo(newRoles);
    }

    @Test
    @DisplayName("Удалить user")
    public void shouldDeleteUser() {
        User user = UserDataUtil.getSimpleUser();
        Task task = TaskDataUtil.getSimpleTask();
        User saveUser = userService.save(user);
        task.setOwner(saveUser);
        Board board = BoardDataUtil.getSimpleBoard();
        board.setOwner(saveUser);
        task.setBoard(board);
        Board savedBoard = boardRepository.save(board);
        taskRepository.save(task);
        Task savedTask = taskRepository.save(task);

        userService.delete(saveUser.getId());

        assertThat(userRepository.findById(saveUser.getId()).isPresent()).isFalse();
        assertThat(taskRepository.findById(savedTask.getId()).isPresent()).isFalse();
        assertThat(boardRepository.findById(savedBoard.getId()).isPresent()).isFalse();
    }

}