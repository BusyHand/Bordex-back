package com.ugrasu.bordexback.service;

import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.entity.Task;
import com.ugrasu.bordexback.entity.User;
import com.ugrasu.bordexback.entity.UserBoardRole;
import com.ugrasu.bordexback.repository.BoardRepository;
import com.ugrasu.bordexback.repository.TaskRepository;
import com.ugrasu.bordexback.repository.UserBoardRoleRepository;
import com.ugrasu.bordexback.repository.UserRepository;
import com.ugrasu.bordexback.utli.TaskDataUtil;
import com.ugrasu.bordexback.utli.UserDataUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Import(PostgreTestcontainerConfig.class)
public class TaskServiceIntegrationTest {

    @Autowired
    TaskService taskService;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    UserBoardRoleRepository userBoardRoleRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        boardRepository.deleteAll();
        taskRepository.deleteAll();
        userBoardRoleRepository.deleteAll();
    }

    @Test
    @DisplayName("Сохранить task")
    public void shouldSaveUser() {
        Task task = TaskDataUtil.getSimpleTask();

        task.ge
    }

}
