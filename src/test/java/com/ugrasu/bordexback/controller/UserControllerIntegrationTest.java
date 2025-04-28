package com.ugrasu.bordexback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.rest.dto.full.UserDto;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import com.ugrasu.bordexback.rest.repository.UserBoardRoleRepository;
import com.ugrasu.bordexback.rest.repository.UserRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgreTestcontainerConfig.class)
public class UserControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private UserBoardRoleRepository userBoardRoleRepository;
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
    @DisplayName("GET /api/users возвращает список пользователей")
    void shouldReturnAllUsers() throws Exception {
        userRepository.save(DataGenerator.getSimpleUser());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("GET /api/users/{id} возвращает пользователя по id")
    void shouldReturnUserById() throws Exception {
        User user = userRepository.save(DataGenerator.getSimpleUser());

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    @DisplayName("POST /api/users сохраняет нового пользователя")
    void shouldSaveUser() throws Exception {
        UserDto userDto = DataGenerator.getSimpleUserDto();

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value(userDto.getUsername()));
    }

    @Test
    @DisplayName("PATCH /api/users/{id} обновляет пользователя")
    void shouldPatchUser() throws Exception {
        User user = userRepository.save(DataGenerator.getSimpleUser());

        UserDto updateDto = DataGenerator.getSimpleUserDto();

        mockMvc.perform(patch("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("DELETE /api/users удаляет пользователя")
    void shouldDeleteUser() throws Exception {
        User user = userRepository.save(DataGenerator.getSimpleUser());

        mockMvc.perform(delete("/api/users/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }
}
