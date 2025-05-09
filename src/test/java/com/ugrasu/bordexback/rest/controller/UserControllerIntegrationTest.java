package com.ugrasu.bordexback.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugrasu.bordexback.auth.dto.AuthDto;
import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.rest.dto.web.full.UserDto;
import com.ugrasu.bordexback.rest.entity.User;
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
    PasswordEncoder passwordEncoder;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BoardRolesRepository boardRolesRepository;

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
    @DisplayName("GET /api/users возвращает список пользователей")
    void shouldReturnAllUsers() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        String accessToken = getAccessToken();
        mockMvc.perform(get("/api/users")
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("GET /api/users/{id} возвращает пользователя по id")
    void shouldReturnUserById() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        String accessToken = getAccessToken();
        mockMvc.perform(get("/api/users/" + user.getId())
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()));
    }

    @Test
    @DisplayName("PATCH /api/users/{id} обновляет пользователя")
    void shouldPatchUser() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        UserDto updateDto = DataGenerator.getSimpleUserDto();

        String accessToken = getAccessToken();
        mockMvc.perform(patch("/api/users/" + user.getId())
                        .cookie(new Cookie("access_token", accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @DisplayName("DELETE /api/users удаляет пользователя")
    void shouldDeleteUser() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        String accessToken = getAccessToken();
        mockMvc.perform(delete("/api/users/" + user.getId())
                        .cookie(new Cookie("access_token", accessToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(userRepository.findById(user.getId())).isEmpty();
    }
}
