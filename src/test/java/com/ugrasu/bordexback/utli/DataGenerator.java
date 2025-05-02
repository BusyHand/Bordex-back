package com.ugrasu.bordexback.utli;

import com.ugrasu.bordexback.rest.dto.web.full.BoardDto;
import com.ugrasu.bordexback.rest.dto.web.full.TaskDto;
import com.ugrasu.bordexback.rest.dto.web.full.UserDto;
import com.ugrasu.bordexback.rest.dto.web.slim.BoardSlimDto;
import com.ugrasu.bordexback.rest.dto.web.slim.UserSlimDto;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.UserBoardRole;
import com.ugrasu.bordexback.rest.entity.enums.*;
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
        BoardDto board = new BoardDto();
        board.setName("Test board");
        board.setDescription("Test description board");
        board.setScope(Scope.PUBLIC);
        return board;
    }

    public static UserDto getSimpleUserDto() {
        UserDto user = new UserDto();
        user.setUsername("testuser");
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setEmail("test@example.com");
        user.setRoles(Set.of(Role.USER));
        return user;
    }

    public static TaskDto getSimpleTaskDto() {
        TaskDto taskDto = new TaskDto();
        taskDto.setName("Test Task");
        taskDto.setDescription("Test description");
        taskDto.setStatus(Status.NEW);
        taskDto.setPriority(Priority.MEDIUM);
        taskDto.setDeadline(LocalDateTime.now().plusDays(1));
        return taskDto;
    }

    public UserSlimDto getSimpleUserSlimDto(User user) {
        UserSlimDto userSlimDto = new UserSlimDto();
        userSlimDto.setId(user.getId());
        userSlimDto.setUsername(user.getUsername());
        userSlimDto.setFirstName(user.getFirstName());
        userSlimDto.setLastName(user.getLastName());
        userSlimDto.setEmail(user.getEmail());
        return userSlimDto;

    }

    public BoardSlimDto getSimpleBoardSlimDto(Board board) {
        BoardSlimDto boardSlimDto = new BoardSlimDto();
        boardSlimDto.setId(board.getId());
        boardSlimDto.setName(board.getName());
        boardSlimDto.setScope(board.getScope());
        return boardSlimDto;
    }

}
