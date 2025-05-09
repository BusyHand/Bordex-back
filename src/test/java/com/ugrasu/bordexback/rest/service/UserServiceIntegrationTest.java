package com.ugrasu.bordexback.rest.service;

import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.Role;
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
    BoardRolesRepository boardRolesRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        boardRepository.deleteAll();
        taskRepository.deleteAll();
        boardRolesRepository.deleteAll();
    }

    @Test
    @DisplayName("Обновить user")
    public void shouldPatchUser() {
        User user = DataGenerator.getSimpleUser();
        String newEmail = "New email";
        String newPassword = "New password";
        String newFirstName = "New first name";
        String newLastName = "New last name";
        String newUsername = "New username";
        Set<Role> newRoles = Set.of(Role.USER, Role.ADMIN);
        User userToPatch = DataGenerator.getSimpleUser();
        userToPatch.setEmail(newEmail);
        userToPatch.setPassword(newPassword);
        userToPatch.setFirstName(newFirstName);
        userToPatch.setLastName(newLastName);
        userToPatch.setUsername(newUsername);
        userToPatch.setRoles(newRoles);


        User save = userRepository.save(user);
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
        User user = DataGenerator.getSimpleUser();
        Task task = DataGenerator.getSimpleTask();
        User saveUser = userRepository.save(user);
        task.setOwner(saveUser);
        Board board = DataGenerator.getSimpleBoard();
        board.setOwner(saveUser);
        task.setBoard(board);
        Board savedBoard = boardRepository.save(board);
        Task savedTask = taskRepository.save(task);

        userService.delete(saveUser.getId());

        assertThat(userRepository.findById(saveUser.getId()).isPresent()).isFalse();
        assertThat(taskRepository.findById(savedTask.getId()).isPresent()).isFalse();
        assertThat(boardRepository.findById(savedBoard.getId()).isPresent()).isFalse();
    }

}