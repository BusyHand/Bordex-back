package com.ugrasu.bordexback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.dto.full.UserBoardRoleDto;
import com.ugrasu.bordexback.entity.Board;
import com.ugrasu.bordexback.entity.User;
import com.ugrasu.bordexback.entity.UserBoardRole;
import com.ugrasu.bordexback.entity.enums.BoardRole;
import com.ugrasu.bordexback.repository.BoardRepository;
import com.ugrasu.bordexback.repository.TaskRepository;
import com.ugrasu.bordexback.repository.UserBoardRoleRepository;
import com.ugrasu.bordexback.repository.UserRepository;
import com.ugrasu.bordexback.utli.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgreTestcontainerConfig.class)
public class UserBoardRoleControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserBoardRoleRepository userBoardRoleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userBoardRoleRepository.deleteAll();
        boardRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /api/users/boards/roles возвращает список ролей")
    void shouldReturnAllUserBoardRoles() throws Exception {
        User simpleUser = DataGenerator.getSimpleUser();
        userRepository.save(simpleUser);
        Board simpleBoard = DataGenerator.getSimpleBoard(simpleUser);
        boardRepository.save(simpleBoard);
        UserBoardRole toSave = DataGenerator.getSimpleUserBoardRole(simpleUser, simpleBoard, BoardRole.VIEWER);
        userBoardRoleRepository.save(toSave);

        mockMvc.perform(get("/api/users/boards/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("GET /api/users/boards/roles/{id} возвращает роль по id")
    void shouldReturnUserBoardRoleById() throws Exception {
        User simpleUser = DataGenerator.getSimpleUser();
        userRepository.save(simpleUser);
        Board simpleBoard = DataGenerator.getSimpleBoard(simpleUser);
        boardRepository.save(simpleBoard);
        UserBoardRole toSave = DataGenerator.getSimpleUserBoardRole(simpleUser, simpleBoard, BoardRole.VIEWER);
        UserBoardRole saved = userBoardRoleRepository.save(toSave);

        mockMvc.perform(get("/api/users/boards/roles/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId()))
                .andExpect(jsonPath("$.user.id").value(simpleUser.getId()))
                .andExpect(jsonPath("$.board.id").value(simpleBoard.getId()));
    }


    @Test
    @DisplayName("POST /api/users/boards/roles/{user-id}/{board-id} сохраняет роль")
    void shouldSaveUserBoardRole() throws Exception {
        User user = userRepository.save(DataGenerator.getSimpleUser());
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(user));
        UserBoardRoleDto dto = new UserBoardRoleDto(
                null,
                null,
                null,
                Set.of(BoardRole.VIEWER)
        );

        mockMvc.perform(post("/api/users/boards/roles/" + user.getId() + "/" + board.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.boardRoles[0]").value("VIEWER"));
    }

    @Test
    @DisplayName("PATCH /api/users/boards/roles/{user-id}/{board-id} обновляет роль")
    void shouldPatchUserBoardRole() throws Exception {
        User user = userRepository.save(DataGenerator.getSimpleUser());
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(user));
        userBoardRoleRepository.save(DataGenerator.getSimpleUserBoardRole(user, board, BoardRole.VIEWER));
        UserBoardRoleDto updatedDto = new UserBoardRoleDto(
                null,
                null,
                null,
                Set.of(BoardRole.MANAGER)
        );

        mockMvc.perform(patch("/api/users/boards/roles/" + user.getId() + "/" + board.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardRoles[0]").value("MANAGER"));
    }

    @Test
    @DisplayName("DELETE /api/users/boards/roles/{user-id}/{board-id}/{role} удаляет роль")
    void shouldDeleteRole() throws Exception {
        User user = userRepository.save(DataGenerator.getSimpleUser());
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(user));
        userBoardRoleRepository.save(DataGenerator.getSimpleUserBoardRole(user, board, BoardRole.VIEWER));

        mockMvc.perform(delete("/api/users/boards/roles/" + user.getId() + "/" + board.getId() + "/VIEWER"))
                .andExpect(status().isNoContent());

        assertThat(userBoardRoleRepository.findAll()).isEmpty();
    }


    @Test
    @DisplayName("DELETE /api/users/boards/roles/{user-id}/{board-id} удаляет все роли")
    void shouldDeleteAllRolesForUserOnBoard() throws Exception {
        User user = userRepository.save(DataGenerator.getSimpleUser());
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(user));
        userBoardRoleRepository.save(DataGenerator.getSimpleUserBoardRole(user, board, BoardRole.VIEWER));

        mockMvc.perform(delete("/api/users/boards/roles/" + user.getId() + "/" + board.getId()))
                .andExpect(status().isNoContent());

        assertThat(userBoardRoleRepository.findAll()).isEmpty();
    }

}
