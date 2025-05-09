package com.ugrasu.bordexback.rest.facade;

import com.ugrasu.bordexback.config.PostgreTestcontainerConfig;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.BoardRoles;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.BoardRole;
import com.ugrasu.bordexback.rest.exception.UserNotBoardMemberException;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import com.ugrasu.bordexback.rest.repository.BoardRolesRepository;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import com.ugrasu.bordexback.util.DataGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
@Import(PostgreTestcontainerConfig.class)
public class BoardFacadeManagementIntegrationTest {

    @Autowired
    BoardFacadeManagement boardFacadeManagement;

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;

    @Autowired
    BoardRolesRepository boardRolesRepository;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        boardRepository.deleteAll();
        taskRepository.deleteAll();
        boardRolesRepository.deleteAll();
    }

    @Test
    @DisplayName("Не должно передать владельца если пользователь не участник доски")
    public void shouldNotTransferOwner() {
        User owner = DataGenerator.getSimpleUser();
        owner = userRepository.save(owner);
        Board board = DataGenerator.getSimpleBoard();
        Board saved = boardFacadeManagement.createBoard(board, owner.getId());
        User newOwner = DataGenerator.getSimpleUser("user", "email");
        User savedNewOwner = userRepository.save(newOwner);

        assertThatThrownBy(() -> boardFacadeManagement.ownerTransfer(saved.getId(), savedNewOwner.getId()))
                .isInstanceOf(UserNotBoardMemberException.class);

    }

    @Test
    @DisplayName("Должно изменить владельца доски который уже есть на доске")
    @Transactional
    public void shouldTransferToNewOwnerAlreadyMember() {
        User owner = DataGenerator.getSimpleUser();
        owner = userRepository.save(owner);
        Board board = DataGenerator.getSimpleBoard();
        board = boardFacadeManagement.createBoard(board, owner.getId());
        User newOwner = DataGenerator.getSimpleUser("user", "email");
        newOwner = userRepository.save(newOwner);
        Board boardWithAddedUser = boardFacadeManagement.addUserToBoard(board.getId(), newOwner.getId());

        boardFacadeManagement.ownerTransfer(board.getId(), newOwner.getId());

        Board saved = boardRepository.findById(boardWithAddedUser.getId()).orElseThrow();
        List<User> boardMembers = saved.getBoardMembers().stream().toList();
        List<BoardRoles> boardRoles = saved.getBoardRoles().stream().toList();
        assertThat(saved.getOwner()).isEqualTo(newOwner);
        assertThat(boardMembers.size()).isEqualTo(2);
        assertThat(boardMembers.contains(owner)).isTrue();
        assertThat(boardMembers.contains(newOwner)).isTrue();
        assertThat(boardRoles.size()).isEqualTo(2);
        assertThat(boardRoles.get(0).getBoardRoles().size()).isEqualTo(3);
        assertThat(boardRoles.get(1).getBoardRoles().size()).isEqualTo(3);
        assertThat(boardRoles.get(0).getBoardRoles()).contains(BoardRole.values());
        assertThat(boardRoles.get(1).getBoardRoles()).contains(BoardRole.values());
    }

    @Test
    @DisplayName("Удалить пользователя с доски")
    @Transactional
    public void shouldRemoveUserFromBoard() {
        User owner = DataGenerator.getSimpleUser();
        owner = userRepository.save(owner);
        Board board = DataGenerator.getSimpleBoard();
        board = boardFacadeManagement.createBoard(board, owner.getId());
        User userToAdd = DataGenerator.getSimpleUser("user", "email");
        userToAdd = userRepository.save(userToAdd);
        Board boardWithAddedUser = boardFacadeManagement.addUserToBoard(board.getId(), userToAdd.getId());

        boardFacadeManagement.removeUserFromBoard(board.getId(), userToAdd.getId());

        Board saved = boardRepository.findById(boardWithAddedUser.getId()).orElseThrow();
        List<User> boardMembers = saved.getBoardMembers().stream().toList();
        List<BoardRoles> boardRoles = saved.getBoardRoles().stream().toList();
        assertThat(boardMembers.size()).isEqualTo(1);
        assertThat(boardMembers.contains(owner)).isTrue();
        assertThat(boardMembers.contains(userToAdd)).isFalse();
        assertThat(boardRoles.size()).isEqualTo(1);
        assertThat(boardRoles.get(0).getBoardRoles().size()).isEqualTo(3);
        assertThat(boardRoles.get(0).getBoardRoles().contains(BoardRole.VIEWER)).isTrue();
        assertThat(boardRoles.get(0).getBoardRoles().contains(BoardRole.MANAGER)).isTrue();
        assertThat(boardRoles.get(0).getBoardRoles().contains(BoardRole.DEVELOPER)).isTrue();
    }

    @Test
    @DisplayName("Добавить пользователя к доске")
    @Transactional
    public void shouldAddUserToBoard() {
        User owner = DataGenerator.getSimpleUser();
        owner = userRepository.save(owner);
        Board board = DataGenerator.getSimpleBoard();
        board = boardFacadeManagement.createBoard(board, owner.getId());
        User userToAdd = DataGenerator.getSimpleUser("user", "email");
        userToAdd = userRepository.save(userToAdd);

        Board boardWithAddedUser = boardFacadeManagement.addUserToBoard(board.getId(), userToAdd.getId());

        Board saved = boardRepository.findById(boardWithAddedUser.getId()).orElseThrow();
        List<User> boardMembers = saved.getBoardMembers().stream().toList();
        List<BoardRoles> boardRoles = saved.getBoardRoles().stream().toList();
        assertThat(boardMembers.size()).isEqualTo(2);
        assertThat(boardMembers.contains(owner)).isTrue();
        assertThat(boardMembers.contains(userToAdd)).isTrue();
        assertThat(boardRoles.size()).isEqualTo(2);
        assertThat(boardRoles.get(0).getBoardRoles().size()).isIn(1, 3);
        assertThat(boardRoles.get(1).getBoardRoles().size()).isIn(1, 3);
        assertThat(boardRoles.get(0).getBoardRoles().contains(BoardRole.VIEWER)).isTrue();
        assertThat(boardRoles.get(1).getBoardRoles().contains(BoardRole.VIEWER)).isTrue();
    }

    @Test
    @DisplayName("Должно создать доску со всеми ролями у владельца и он должен быть в пользователях у доски")
    @Transactional
    public void shouldCreateBoard() {
        User owner = DataGenerator.getSimpleUser();
        owner = userRepository.save(owner);
        Board board = DataGenerator.getSimpleBoard();

        Board created = boardFacadeManagement.createBoard(board, owner.getId());

        Board saved = boardRepository.findById(created.getId()).orElseThrow();
        assertThat(saved.getOwner()).isEqualTo(owner);
        assertThat(saved.getName()).isEqualTo(board.getName());
        assertThat(saved.getDescription()).isEqualTo(board.getDescription());
        assertThat(saved.getScope()).isEqualTo(board.getScope());
        assertThat(saved.getBoardMembers().isEmpty()).isFalse();
        assertThat(saved.getBoardMembers().contains(owner)).isTrue();
        assertThat(saved.getBoardRoles().isEmpty()).isFalse();
        assertThat(saved.getBoardRoles().stream().toList().get(0).getBoard().getId()).isEqualTo(created.getId());
        assertThat(saved.getBoardRoles().stream().toList().get(0).getUser().getId()).isEqualTo(owner.getId());
        assertThat(saved.getBoardRoles().stream().toList().get(0).getBoardRoles()).contains(BoardRole.values());
    }
}
