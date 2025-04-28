package com.ugrasu.bordexback.service;

import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.Scope;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import com.ugrasu.bordexback.rest.repository.UserBoardRoleRepository;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import com.ugrasu.bordexback.rest.service.BoardService;
import com.ugrasu.bordexback.utli.DataGenerator;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Import(PostgreTestcontainerConfig.class)
public class BoardServiceIntegrationTest {

    @Autowired
    BoardService boardService;

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
    @DisplayName("Сохранить board")
    public void shouldSaveBoardWithOwner() {
        Board simpleBoard = DataGenerator.getSimpleBoard();
        User owner = DataGenerator.getSimpleUser();
        owner = userRepository.save(owner);


        Board saved = boardService.save(simpleBoard, owner);


        assertThat(saved.getName()).isEqualTo(simpleBoard.getName());
        assertThat(saved.getDescription()).isEqualTo(simpleBoard.getDescription());
        assertThat(saved.getOwner()).isEqualTo(owner);
        assertThat(owner.getId()).isEqualTo(saved.getOwner().getId());
    }

    @Test
    @DisplayName("Сохранить board , затем обновить name и desc")
    public void shouldUpdateBoardNameAndDescription() {
        String updatedName = "New name";
        String updateDescription = "New description";
        Board simpleBoard = DataGenerator.getSimpleBoard();
        User owner = DataGenerator.getSimpleUser();
        owner = userRepository.save(owner);


        Board saved = boardService.save(simpleBoard, owner);
        Board updated = DataGenerator.getSimpleBoard();
        updated.setName(updatedName);
        updated.setDescription(updateDescription);
        Board patched = boardService.patch(saved.getId(), updated);


        assertThat(patched.getScope()).isEqualTo(saved.getScope());
        assertThat(patched.getName()).isEqualTo(updatedName);
        assertThat(patched.getDescription()).isEqualTo(updateDescription);
        assertThat(patched.getOwner()).isEqualTo(owner);
        assertThat(owner.getId()).isEqualTo(patched.getOwner().getId());
    }

    @Test
    @DisplayName("Сохранить board, затем обновить scope")
    public void shouldUpdateScope() {
        Scope updateScopePrivate = Scope.PRIVATE;
        Board simpleBoard = DataGenerator.getSimpleBoard();
        User owner = DataGenerator.getSimpleUser();
        owner = userRepository.save(owner);


        Board saved = boardService.save(simpleBoard, owner);
        Board toPatch = DataGenerator.getSimpleBoard();
        toPatch.setScope(updateScopePrivate);
        Board patched = boardService.patch(saved.getId(), toPatch);


        assertThat(patched.getScope()).isEqualTo(updateScopePrivate);
        assertThat(patched.getName()).isEqualTo(saved.getName());
        assertThat(patched.getDescription()).isEqualTo(saved.getDescription());
        assertThat(patched.getOwner()).isEqualTo(owner);
        assertThat(owner.getId()).isEqualTo(patched.getOwner().getId());
    }

    @Test
    @DisplayName("Сохранить board, затем обновить owner")
    public void shouldUpdateOwner() {
        Board simpleBoard = DataGenerator.getSimpleBoard();
        User oldOwner = DataGenerator.getSimpleUser();
        oldOwner = userRepository.save(oldOwner);
        User newOwner = DataGenerator.getSimpleUser();
        newOwner.setEmail("new Email");
        newOwner = userRepository.save(newOwner);


        Board saved = boardService.save(simpleBoard, oldOwner);
        Board updated = DataGenerator.getSimpleBoard();
        updated.setOwner(newOwner);
        Board patched = boardService.patch(saved.getId(), updated);


        assertThat(patched.getScope()).isEqualTo(saved.getScope());
        assertThat(patched.getName()).isEqualTo(saved.getName());
        assertThat(patched.getDescription()).isEqualTo(saved.getDescription());
        assertThat(patched.getOwner()).isEqualTo(newOwner);
    }

    @Test
    @DisplayName("Сохранить board, затем удалить")
    public void shouldDeleteBoard() {
        Board simpleBoard = DataGenerator.getSimpleBoard();
        User owner = DataGenerator.getSimpleUser();
        owner = userRepository.save(owner);
        Task firstTask = DataGenerator.getSimpleTask();
        Task secondTask = DataGenerator.getSimpleTask();


        Board saved = boardService.save(simpleBoard, owner);
        firstTask.setOwner(owner);
        secondTask.setOwner(owner);
        firstTask.setBoard(saved);
        secondTask.setBoard(saved);
        taskRepository.save(firstTask);
        taskRepository.save(secondTask);
        boardService.delete(saved.getId());


        assertThatThrownBy(() -> boardService.findOne(saved.getId()))
                .isInstanceOf(EntityNotFoundException.class);
        assertThat(taskRepository.findById(firstTask.getId()).isPresent()).isFalse();
        assertThat(taskRepository.findById(secondTask.getId()).isPresent()).isFalse();
        assertThat(userRepository.findById(owner.getId()).isPresent()).isTrue();
    }


}
