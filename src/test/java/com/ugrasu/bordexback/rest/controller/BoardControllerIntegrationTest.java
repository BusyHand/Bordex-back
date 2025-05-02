package com.ugrasu.bordexback.rest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.config.TestConfigurationSecurity;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import com.ugrasu.bordexback.rest.repository.UserBoardRoleRepository;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import com.ugrasu.bordexback.util.DataGenerator;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Import({TestConfigurationSecurity.class, PostgreTestcontainerConfig.class})
public class BoardControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserBoardRoleRepository userBoardRoleRepository;
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
    @DisplayName("GET /api/boards возвращает список досок")
    void shouldReturnAllBoards() throws Exception {
        User simpleUser = DataGenerator.getSimpleUser();
        userRepository.save(simpleUser);
        boardRepository.save(DataGenerator.getSimpleBoard(simpleUser));

        mockMvc.perform(get("/api/boards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("GET /api/boards/{id} возвращает доску по id")
    void shouldReturnBoardById() throws Exception {
        User simpleUser = DataGenerator.getSimpleUser();
        userRepository.save(simpleUser);
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(simpleUser));

        mockMvc.perform(get("/api/boards/" + board.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(board.getId()));
    }

    //TODO with log user
   /* @Test
    @DisplayName("POST /api/boards сохраняет новую доску")
    void shouldSaveBoard() throws Exception {
        BoardDto boardDto = DataGenerator.getSimpleBoardDto();

        mockMvc.perform(post("/api/boards")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(boardDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Test board"));
    }*/

    /*@Test
    @DisplayName("PATCH /api/boards/{id} обновляет доску")
    void shouldPatchBoard() throws Exception {

        BoardDto boardDto = DataGenerator.getSimpleBoardDto();
        board = boardRepository.save(board);

        BoardDto updateDto = new BoardDto();
        updateDto.setTitle("New title");

        mockMvc.perform(patch("/api/boards/" + board.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New title"));
    }*/

    @Test
    @DisplayName("DELETE /api/boards удаляет доску")
    void shouldDeleteBoard() throws Exception {
        User simpleUser = DataGenerator.getSimpleUser();
        userRepository.save(simpleUser);
        Board board = boardRepository.save(DataGenerator.getSimpleBoard(simpleUser));

        mockMvc.perform(delete("/api/boards/" + board.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(boardRepository.findById(board.getId())).isEmpty();
    }
}
