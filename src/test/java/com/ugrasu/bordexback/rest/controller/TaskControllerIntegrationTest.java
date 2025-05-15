package com.ugrasu.bordexback.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugrasu.bordexback.auth.dto.AuthDto;
import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.rest.dto.web.full.TaskDto;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import com.ugrasu.bordexback.rest.entity.enums.Role;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.BoardRolesRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgreTestcontainerConfig.class)
public class TaskControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRolesRepository boardRolesRepository;

    @Autowired
    BoardRepository boardRepository;

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
    @DisplayName("GET /api/tasks возвращает список задач")
    void shouldReturnAllTasks() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(Role.values()));
        User savedUser = userRepository.save(user);
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(savedUser));
        boardRolesRepository.save(DataGenerator.getSimpleUserBoardRole(user, board, BoardRole.values()));
        taskRepository.save(DataGenerator.getSimpleTask(savedUser, board));

        String accessToken = getAccessToken();
        mockMvc.perform(get("/api/tasks")
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("GET /api/tasks/{id} возвращает задачу по id")
    void shouldReturnTaskById() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(savedUser));
        Task task = taskRepository.save(DataGenerator.getSimpleTask(user, board));
        boardRolesRepository.save(DataGenerator.getSimpleUserBoardRole(user, board, BoardRole.values()));

        String accessToken = getAccessToken();
        mockMvc.perform(get("/api/tasks/" + task.getId())
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()));
    }

    @Test
    @DisplayName("POST /api/tasks/{board-id} сохраняет новую задачу")
    void shouldSaveTask() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(savedUser));
        boardRolesRepository.save(DataGenerator.getSimpleUserBoardRole(user, board, BoardRole.values()));
        TaskDto taskDto = DataGenerator.getSimpleTaskDto();

        String accessToken = getAccessToken();
        mockMvc.perform(post("/api/tasks/" + board.getId())
                        .cookie(new Cookie("access_token", accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(taskDto.getName()));
    }


    @Test
    @DisplayName("PATCH /api/tasks/{id} обновляет задачу")
    void shouldPatchTask() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(savedUser));
        Task task = taskRepository.save(DataGenerator.getSimpleTask(user, board));
        boardRolesRepository.save(DataGenerator.getSimpleUserBoardRole(user, board, BoardRole.values()));
        TaskDto updateDto = DataGenerator.getSimpleTaskDto();

        String accessToken = getAccessToken();
        mockMvc.perform(patch("/api/tasks/" + task.getId())
                        .cookie(new Cookie("access_token", accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateDto.getName()));
    }

    @Test
    @DisplayName("DELETE /api/tasks удаляет задачу")
    void shouldDeleteTask() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(savedUser));
        Task task = taskRepository.save(DataGenerator.getSimpleTask(user, board));
        boardRolesRepository.save(DataGenerator.getSimpleUserBoardRole(user, board, BoardRole.values()));

        String accessToken = getAccessToken();
        mockMvc.perform(delete("/api/tasks/" + task.getId())
                        .cookie(new Cookie("access_token", accessToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(taskRepository.findById(task.getId())).isEmpty();
    }
}
