package com.ugrasu.bordexback.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugrasu.bordexback.auth.dto.AuthDto;
import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.rest.dto.web.full.BoardRolesDto;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.BoardRoles;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import com.ugrasu.bordexback.rest.entity.enums.Role;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import com.ugrasu.bordexback.rest.repository.BoardRolesRepository;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import com.ugrasu.bordexback.util.DataGenerator;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class BoardRolesControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    BoardRolesRepository boardRolesRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    TaskRepository taskRepository;

    User authUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        boardRolesRepository.deleteAll();
        boardRepository.deleteAll();
        taskRepository.deleteAll();
        authUser = DataGenerator.getSimpleUser();
    }

    private String getAccessToken() throws Exception {
        AuthDto authDto = new AuthDto();
        authDto.setUsername(authUser.getEmail());
        authDto.setPassword(authUser.getPassword());

        var result = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(authDto)))
                .andExpect(status().isOk())
                .andReturn();

        String[] cookies = result.getResponse().getHeaders("Set-Cookie").toArray(new String[0]);

        for (String cookie : cookies) {
            if (cookie.startsWith("access_token=")) {
                return cookie.split(";", 2)[0].substring("access_token=".length());
            }
        }
        throw new RuntimeException("access_token cookie not found");
    }

    @Test
    @DisplayName("GET /api/users/boards/roles возвращает список ролей")
    void shouldReturnAllUserBoardRoles() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.values()));
        userRepository.save(user);
        Board simpleBoard = DataGenerator.getSimpleBoard(user);
        boardRepository.save(simpleBoard);
        BoardRoles toSave = DataGenerator.getSimpleUserBoardRole(user, simpleBoard, BoardRole.VIEWER);
        boardRolesRepository.save(toSave);

        String accessToken = getAccessToken();

        mockMvc.perform(get("/api/users/boards/roles")
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("PATCH /api/users/boards/roles/{user-id}/{board-id} обновляет роль")
    void shouldPatchUserBoardRole() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        Board simpleBoard = DataGenerator.getSimpleBoard(user);
        boardRepository.save(simpleBoard);
        boardRolesRepository.save(DataGenerator.getSimpleUserBoardRole(user, simpleBoard, BoardRole.VIEWER));
        BoardRolesDto updatedDto = new BoardRolesDto(
                null,
                null,
                null,
                Set.of(BoardRole.MANAGER)
        );

        String accessToken = getAccessToken();

        mockMvc.perform(patch("/api/users/boards/roles/" + user.getId() + "/" + simpleBoard.getId())
                        .cookie(new Cookie("access_token", accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.boardRoles[0]").value("MANAGER"));
    }

    @Test
    @DisplayName("DELETE /api/users/boards/roles/{user-id}/{board-id}/{role} удаляет роль")
    void shouldDeleteRole() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        Board simpleBoard = DataGenerator.getSimpleBoard(user);
        boardRepository.save(simpleBoard);
        boardRolesRepository.save(DataGenerator.getSimpleUserBoardRole(user, simpleBoard, BoardRole.VIEWER));

        String accessToken = getAccessToken();

        mockMvc.perform(delete("/api/users/boards/roles/" + user.getId() + "/" + simpleBoard.getId() + "/VIEWER")
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isNoContent());

        assertThat(boardRolesRepository.findAll()).isEmpty();
    }

}
