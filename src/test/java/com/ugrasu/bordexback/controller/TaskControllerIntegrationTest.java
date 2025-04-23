package com.ugrasu.bordexback.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.dto.full.TaskDto;
import com.ugrasu.bordexback.entity.Board;
import com.ugrasu.bordexback.entity.Task;
import com.ugrasu.bordexback.entity.User;
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
    ObjectMapper objectMapper;

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserBoardRoleRepository userBoardRoleRepository;
    @Autowired
    private BoardRepository boardRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userBoardRoleRepository.deleteAll();
        boardRepository.deleteAll();
        taskRepository.deleteAll();
    }

    @Test
    @DisplayName("GET /api/tasks возвращает список задач")
    void shouldReturnAllTasks() throws Exception {
        User simpleUser = DataGenerator.getSimpleUser();
        simpleUser = userRepository.save(simpleUser);
        Board simpleBoard = DataGenerator.getSimpleBoard(simpleUser);
        simpleBoard = boardRepository.save(simpleBoard);

        taskRepository.save(DataGenerator.getSimpleTask(simpleUser, simpleBoard));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)));
    }

    @Test
    @DisplayName("GET /api/tasks/{id} возвращает задачу по id")
    void shouldReturnTaskById() throws Exception {
        User simpleUser = DataGenerator.getSimpleUser();
        simpleUser = userRepository.save(simpleUser);
        Board simpleBoard = DataGenerator.getSimpleBoard(simpleUser);
        simpleBoard = boardRepository.save(simpleBoard);
        Task task = taskRepository.save(DataGenerator.getSimpleTask(simpleUser, simpleBoard));

        mockMvc.perform(get("/api/tasks/" + task.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(task.getId()));
    }

    //TODO with logged user
    /*@Test
    @DisplayName("POST /api/tasks/{board-id} сохраняет новую задачу")
    void shouldSaveTask() throws Exception {
        User simpleUser = DataGenerator.getSimpleUser();
        simpleUser = userRepository.save(simpleUser);
        Board simpleBoard = DataGenerator.getSimpleBoard(simpleUser);
        simpleBoard = boardRepository.save(simpleBoard);

        TaskDto taskDto = DataGenerator.getSimpleTaskDto();

        mockMvc.perform(post("/api/tasks/" + simpleBoard.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(taskDto.getName()));
    }*/


    @Test
    @DisplayName("PATCH /api/tasks/{id} обновляет задачу")
    void shouldPatchTask() throws Exception {
        User simpleUser = DataGenerator.getSimpleUser();
        simpleUser = userRepository.save(simpleUser);
        Board simpleBoard = DataGenerator.getSimpleBoard(simpleUser);
        simpleBoard = boardRepository.save(simpleBoard);
        Task task = taskRepository.save(DataGenerator.getSimpleTask(simpleUser, simpleBoard));
        TaskDto updateDto = DataGenerator.getSimpleTaskDto();

        mockMvc.perform(patch("/api/tasks/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(updateDto.getName()));
    }

    @Test
    @DisplayName("DELETE /api/tasks удаляет задачу")
    void shouldDeleteTask() throws Exception {
        User simpleUser = DataGenerator.getSimpleUser();
        simpleUser = userRepository.save(simpleUser);
        Board simpleBoard = DataGenerator.getSimpleBoard(simpleUser);
        simpleBoard = boardRepository.save(simpleBoard);
        Task task = taskRepository.save(DataGenerator.getSimpleTask(simpleUser, simpleBoard));

        mockMvc.perform(delete("/api/tasks/" + task.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        assertThat(taskRepository.findById(task.getId())).isEmpty();
    }
}
