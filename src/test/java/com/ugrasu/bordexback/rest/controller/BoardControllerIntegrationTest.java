package com.ugrasu.bordexback.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugrasu.bordexback.auth.dto.AuthDto;
import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.rest.dto.web.full.BoardDto;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.facade.BoardFacadeManagement;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import com.ugrasu.bordexback.rest.repository.UserBoardRoleRepository;
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
public class BoardControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    BoardFacadeManagement boardFacadeManagement;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserBoardRoleRepository userBoardRoleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TaskRepository taskRepository;

    User authUser;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userBoardRoleRepository.deleteAll();
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
    @DisplayName("GET /api/boards возвращает список досок")
    void shouldReturnAllBoards() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        boardRepository.save(DataGenerator.getSimpleBoard(savedUser));
        String accessToken = getAccessToken();

        mockMvc.perform(get("/api/boards")
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }


    @Test
    @DisplayName("GET /api/boards/{id} возвращает доску по id")
    void shouldReturnBoardById() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(savedUser));
        String accessToken = getAccessToken();

        mockMvc.perform(get("/api/boards/" + board.getId())
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(board.getId()));
    }

    @Test
    @DisplayName("POST /api/boards сохраняет новую доску")
    void shouldSaveBoard() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        BoardDto boardDto = DataGenerator.getSimpleBoardDto();
        String accessToken = getAccessToken();

        mockMvc.perform(post("/api/boards")
                        .cookie(new Cookie("access_token", accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test board"));
    }

    @Test
    @DisplayName("PATCH /api/boards/{id} обновляет доску")
    void shouldPatchBoard() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(savedUser));
        String accessToken = getAccessToken();

        BoardDto updateDto = new BoardDto();
        updateDto.setName("New name");

        mockMvc.perform(patch("/api/boards/" + board.getId())
                        .cookie(new Cookie("access_token", accessToken))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New name"));
    }

    @Test
    @DisplayName("DELETE /api/boards удаляет доску")
    void shouldDeleteBoard() throws Exception {
        User user = DataGenerator.getSimpleUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(savedUser));
        String accessToken = getAccessToken();

        mockMvc.perform(delete("/api/boards/" + board.getId())
                        .cookie(new Cookie("access_token", accessToken))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(boardRepository.findById(board.getId())).isEmpty();
    }

    @Test
    @DisplayName("PATCH /api/boards/{id}/add-user/{userId} добавляет пользователя к доске")
    void shouldAddUserToBoard() throws Exception {
        User owner = DataGenerator.getSimpleUser();
        owner.setPassword(passwordEncoder.encode(owner.getPassword()));
        userRepository.save(owner);

        User anotherUser = new User();
        anotherUser.setEmail("another@example.com");
        anotherUser.setUsername("another");
        anotherUser.setPassword(passwordEncoder.encode("password"));
        userRepository.save(anotherUser);

        Board board = boardRepository.save(DataGenerator.getSimpleBoard(owner));
        String accessToken = getAccessToken();

        mockMvc.perform(patch("/api/boards/" + board.getId() + "/add-user/" + anotherUser.getId())
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(board.getId()));
    }

    @Test
    @DisplayName("PATCH /api/boards/{id}/remove-user/{userId} удаляет пользователя с доски")
    void shouldRemoveUserFromBoard() throws Exception {
        User owner = DataGenerator.getSimpleUser();
        owner.setPassword(passwordEncoder.encode(owner.getPassword()));
        userRepository.save(owner);

        User member = new User();
        member.setEmail("member@example.com");
        member.setUsername("another");
        member.setPassword(passwordEncoder.encode("password"));
        userRepository.save(member);

        Board board = boardRepository.save(DataGenerator.getSimpleBoard(owner));
        boardFacadeManagement.addUserToBoard(board.getId(), member.getId());
        String accessToken = getAccessToken();

        mockMvc.perform(patch("/api/boards/" + board.getId() + "/remove-user/" + member.getId())
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(board.getId()));
    }

    @Test
    @DisplayName("PATCH /api/boards/{id}/owner-transfer/{newOwnerId} передаёт владение доской")
    void shouldTransferBoardOwnership() throws Exception {
        User owner = DataGenerator.getSimpleUser();
        owner.setPassword(passwordEncoder.encode(owner.getPassword()));
        userRepository.save(owner);

        User newOwner = new User();
        newOwner.setEmail("newowner@example.com");
        newOwner.setUsername("newowner");
        newOwner.setPassword(passwordEncoder.encode("password"));
        userRepository.save(newOwner);

        Board board = DataGenerator.getSimpleBoard(owner);
        boardFacadeManagement.createBoard(board, owner.getId());
        boardFacadeManagement.addUserToBoard(board.getId(), newOwner.getId());
        String accessToken = getAccessToken();

        mockMvc.perform(patch("/api/boards/" + board.getId() + "/owner-transfer/" + newOwner.getId())
                        .cookie(new Cookie("access_token", accessToken)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(board.getId()));
    }


}
