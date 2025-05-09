package com.ugrasu.bordexback.util;

import com.ugrasu.bordexback.rest.dto.web.full.BoardDto;
import com.ugrasu.bordexback.rest.dto.web.full.TaskDto;
import com.ugrasu.bordexback.rest.dto.web.full.UserDto;
import com.ugrasu.bordexback.rest.dto.web.slim.BoardSlimDto;
import com.ugrasu.bordexback.rest.dto.web.slim.UserSlimDto;
import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.BoardRoles;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.*;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@UtilityClass
public class DataGenerator {

    public Board getSimpleBoard() {
        return Board.builder()
                .name("Test board")
                .description("Test description board")
                .scope(Scope.PUBLIC)
                .boardRoles(new LinkedHashSet<>())
                .boardMembers(new LinkedHashSet<>())
                .build();
    }

    public Board getSimpleBoard(User user) {
        return Board.builder()
                .owner(user)
                .name("Test board")
                .description("Test description board")
                .boardRoles(new LinkedHashSet<>())
                .boardMembers(new LinkedHashSet<>())
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
                .assignees(new LinkedHashSet<>())
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

    public BoardRoles getSimpleUserBoardRole(User user, Board board, BoardRole... roles) {
        return BoardRoles.builder()
                .user(user)
                .board(board)
                .boardRoles(new HashSet<>(Set.of(roles)))
                .build();
    }

    public User getSimpleUser() {
        return User.builder()
                .username("UserName")
                .password("Password")
                .firstName("FirstName")
                .lastName("LastName")
                .email("cool908yan@yandex.ru")
                .roles(Set.of(Role.USER))
                .assigneesTask(new LinkedHashSet<>())
                .boardsRoles(new LinkedHashSet<>())
                .ownBoards(new LinkedHashSet<>())
                .memberBoards(new LinkedHashSet<>())
                .build();
    }

    public User getSimpleUser(String username, String Email) {
        return User.builder()
                .username(username)
                .password("Password")
                .firstName("FirstName")
                .lastName("LastName")
                .email(Email)
                .roles(Set.of(Role.USER))
                .assigneesTask(new LinkedHashSet<>())
                .boardsRoles(new LinkedHashSet<>())
                .ownBoards(new LinkedHashSet<>())
                .memberBoards(new LinkedHashSet<>())
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
        user.setEmail("cool908yan@yandex.ru");
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
        taskDto.setProgress(2);
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
