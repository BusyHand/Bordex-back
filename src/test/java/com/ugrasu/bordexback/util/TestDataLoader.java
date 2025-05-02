package com.ugrasu.bordexback.util;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.UserBoardRole;
import com.ugrasu.bordexback.rest.entity.enums.*;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import com.ugrasu.bordexback.rest.repository.UserBoardRoleRepository;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class TestDataLoader {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final TaskRepository taskRepository;
    private final UserBoardRoleRepository userBoardRoleRepository;

    @Transactional
    public void load() {
        List<User> users = getUsers(2);
        users = userRepository.saveAll(users);

        Board board1 = getBoard(users.get(0), "Test Board 1", "Description 1");
        Board board2 = getBoard(users.get(1), "Test Board 2", "Description 2");

        Board savedBoard1 = boardRepository.save(board1);
        Board savedBoard2 = boardRepository.save(board2);

        List<User> firstUsers = new ArrayList<>(users.subList(0, 1));
        List<User> secondUsers = new ArrayList<>(users.subList(1, 2));

        savedBoard1.setBoardUsers(new HashSet<>(firstUsers));
        savedBoard2.setBoardUsers(new HashSet<>(secondUsers));

        firstUsers.forEach(user -> user.getUserBoards().add(savedBoard1));
        secondUsers.forEach(user -> user.getUserBoards().add(savedBoard2));

        userRepository.saveAll(firstUsers);
        userRepository.saveAll(secondUsers);

        createUserBoardRoles(firstUsers, savedBoard1);
        createUserBoardRoles(secondUsers, savedBoard2);

        List<Task> tasks1 = getTasks(firstUsers, savedBoard1);
        List<Task> tasks2 = getTasks(secondUsers, savedBoard2);

        taskRepository.saveAll(tasks1);
        taskRepository.saveAll(tasks2);
    }

    private void createUserBoardRoles(List<User> users, Board board) {
        List<UserBoardRole> roles = new ArrayList<>();
        for (User user : users) {
            roles.add(UserBoardRole.builder()
                    .user(user)
                    .board(board)
                    .boardRoles(new HashSet<>(Set.of(BoardRole.VIEWER, BoardRole.MANAGER)))
                    .build());
        }
        userBoardRoleRepository.saveAll(roles);
    }

    private List<Task> getTasks(List<User> users, Board board) {
        List<Task> tasks = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Task task = new Task();
            task.setBoard(board);
            task.setOwner(users.get(random(0, users.size() - 1)));
            task.setAssignees(getAssignees(users));
            task.setPriority(randomEnum(Priority.class));
            task.setStatus(randomEnum(Status.class));
            task.setName("Task " + (i + 1));
            task.setDeadline(LocalDateTime.now().plusDays(1));
            task.setDescription("Test task");
            task.setTag(randomEnum(Tag.class));
            task.setProgress(random(0, 100));
            tasks.add(task);
        }
        return tasks;
    }

    private Set<User> getAssignees(List<User> users) {
        Set<User> assignees = new HashSet<>();
        assignees.add(users.get(0));
        return assignees;
    }

    private <T extends Enum<T>> T randomEnum(Class<T> enumClass) {
        T[] values = enumClass.getEnumConstants();
        return values[random(0, values.length - 1)];
    }

    private int random(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private List<User> getUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setUsername("test_user_" + i);
            user.setFirstName("First" + i);
            user.setLastName("Last" + i);
            user.setEmail("user" + i + "@test.com");
            user.setRoles(new HashSet<>(Set.of(randomEnum(Role.class))));
            users.add(user);
        }
        return users;
    }

    private Board getBoard(User owner, String name, String description) {
        Board board = new Board();
        board.setOwner(owner);
        board.setScope(randomEnum(Scope.class));
        board.setName(name);
        board.setDescription(description);
        return board;
    }
}
