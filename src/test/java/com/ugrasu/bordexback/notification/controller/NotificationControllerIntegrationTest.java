package com.ugrasu.bordexback.notification.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.ugrasu.bordexback.auth.dto.AuthDto;
import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.facade.TaskFacadeManagement;
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
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgreTestcontainerConfig.class)
public class NotificationControllerIntegrationTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    TaskFacadeManagement taskFacadeManagement;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRolesRepository boardRolesRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

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
    @DisplayName("GET /api/notifications?userId= возвращает список нотификаций пользователей")
    void shouldGetNewNotification() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(savedUser));
        Task task = DataGenerator.getSimpleTask();
        Task created = taskFacadeManagement.createTask(board.getId(), user.getId(), task);
        taskFacadeManagement.assignUserToTask(created.getId(), savedUser.getId());
        String accessToken = getAccessToken();

        mockMvc.perform(get("/api/notifications?userId={userId}", savedUser.getId())
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("DELETE /api/notifications/{notification-id}?userId={userId} удаляет только для одного пользователя")
    void shouldDeleteOnlyForOneUser() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(savedUser));
        Task task = DataGenerator.getSimpleTask();
        Task created = taskFacadeManagement.createTask(board.getId(), savedUser.getId(), task);
        taskFacadeManagement.assignUserToTask(created.getId(), savedUser.getId());

        User newUser = DataGenerator.getSimpleUser("newUser", "email");
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        User savedNewUser = userRepository.save(newUser);
        taskFacadeManagement.assignUserToTask(created.getId(), savedNewUser.getId());

        String accessToken = getAccessToken();

        MvcResult result = mockMvc.perform(get("/api/notifications?userId=" + savedUser.getId())
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        Long notificationId = ((Number) JsonPath.read(response, "$.content[0].id")).longValue();

        mockMvc.perform(get("/api/notifications?userId=" + savedUser.getId())
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)));

        mockMvc.perform(delete("/api/notifications/{id}?userId={userId}", notificationId, savedUser.getId())
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/notifications?userId=" + savedUser.getId())
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));

        mockMvc.perform(get("/api/notifications?userId=" + savedNewUser.getId())
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

}
