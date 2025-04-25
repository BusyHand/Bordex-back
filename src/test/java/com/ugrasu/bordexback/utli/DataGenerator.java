package com.ugrasu.bordexback.utli;

import com.ugrasu.bordexback.dto.full.BoardDto;
import com.ugrasu.bordexback.dto.full.TaskDto;
import com.ugrasu.bordexback.dto.full.UserDto;
import com.ugrasu.bordexback.dto.slim.BoardSlimDto;
import com.ugrasu.bordexback.dto.slim.UserSlimDto;
import com.ugrasu.bordexback.entity.Board;
import com.ugrasu.bordexback.entity.Task;
import com.ugrasu.bordexback.entity.User;
import com.ugrasu.bordexback.entity.UserBoardRole;
import com.ugrasu.bordexback.entity.enums.*;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.Set;

@UtilityClass
public class DataGenerator {

    public Board getSimpleBoard() {
        return Board.builder()
                .name("Test board")
                .description("Test description board")
                .scope(Scope.PUBLIC)
                .build();
    }

    public Board getSimpleBoard(User user) {
        return Board.builder()
                .owner(user)
                .name("Test board")
                .description("Test description board")
                .scope(Scope.PUBLIC)
                .build();
    }

    public Task getSimpleTask() {
        return Task.builder()
                .name("Task")
                .description("Task")
                .deadline(LocalDateTime.now().plusDays(1))
                .status(Status.NEW)
                .priority(Priority.MEDIUM)
                .build();

    }

    public Task getSimpleTask(User user, Board board) {
        return Task.builder()
                .owner(user)
                .board(board)
                .name("Task")
                .description("Task")
                .deadline(LocalDateTime.now().plusDays(1))
                .status(Status.NEW)
                .priority(Priority.MEDIUM)
                .build();

    }

    public UserBoardRole getSimpleUserBoardRole(User user, Board board, BoardRole... roles) {
        return UserBoardRole.builder()
                .user(user)
                .board(board)
                .boardRoles(Set.of(roles))
                .build();
    }

    public User getSimpleUser() {
        return User.builder()
                .username("UserName")
                .password("Password")
                .firstName("FirstName")
                .lastName("LastName")
                .email("Email")
                .roles(Set.of(Role.USER))
                .build();
    }

    public BoardDto getSimpleBoardDto() {
        return new BoardDto(
                null,
                "Name",
                "description",
                Scope.PUBLIC,
                null,
                null,
                null,
                null
        );
    }

    public static UserDto getSimpleUserDto() {
        return new UserDto(
                null,
                "testuser",
                "Test",
                "User",
                "testuser@example.com",
                Set.of(Role.USER),
                null,
                null,
                null,
                null,
                null
        );
    }

    public static TaskDto getSimpleTaskDto() {
        return new TaskDto(
                null,
                "Test Task",
                "Test description",
                Status.NEW,
                Priority.MEDIUM,
                LocalDateTime.now().plusDays(3),
                null,
                null
        );
    }

    public UserSlimDto getSimpleUserSlimDto(User user) {
        return new UserSlimDto(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRoles()
        );
    }

    public BoardSlimDto getSimpleBoardSlimDto(Board board) {
        return new BoardSlimDto(
                board.getId(),
                board.getName(),
                board.getDescription(),
                board.getScope()
        );
    }

}
