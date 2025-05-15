package com.ugrasu.bordexback.rest.runner;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.BoardRoles;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.*;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
import com.ugrasu.bordexback.rest.repository.BoardRolesRepository;
import com.ugrasu.bordexback.rest.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BoardRepository boardRepository;
    private final TaskRepository taskRepository;
    private final BoardRolesRepository boardRolesRepository;
    private final PasswordEncoder encoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        List<User> users = getUsers(20);
        users = userRepository.saveAll(users);

        Board board1 = getBoard(users.get(0), "Доска номер один", "Описание доски номер один");
        Board board2 = getBoard(users.get(1), "Доска номер два", "Описание доски номер два");

        Board savedBoard1 = boardRepository.save(board1);
        Board savedBoard2 = boardRepository.save(board2);

        List<User> firstUsers = users.stream()
                .limit(10)
                .toList();

        List<User> secondUsers = new ArrayList<>(users.stream()
                .skip(10)
                .limit(10)
                .toList());

        savedBoard1.setBoardMembers(new HashSet<>(firstUsers));
        savedBoard2.setBoardMembers(new HashSet<>(secondUsers));

        firstUsers.forEach(user -> user.getMemberBoards().add(savedBoard1));
        secondUsers.forEach(user -> user.getMemberBoards().add(savedBoard2));

        secondUsers.remove(users.get(0));
        userRepository.saveAll(firstUsers);
        userRepository.saveAll(secondUsers);

        createUserBoardRoles(firstUsers, savedBoard1);
        createUserBoardRoles(secondUsers, savedBoard2);

        List<Task> tasks1 = getTasks(firstUsers, savedBoard1);
        List<Task> tasks2 = getTasks(secondUsers, savedBoard2);

        taskRepository.saveAll(tasks1);
        taskRepository.saveAll(tasks2);


        User me = userRepository.findById(1L).get();
        me.setPassword(encoder.encode("123456"));
        me.setEmail("cool908yan@yandex.ru");
        userRepository.save(me);
        Board board = boardRepository.findById(2L).get();
        BoardRoles boardRoles = BoardRoles.builder()
                .user(me)
                .board(board)
                .boardRoles(Set.of(BoardRole.VIEWER, BoardRole.DEVELOPER, BoardRole.MANAGER))
                .build();
        board.getBoardMembers().add(me);
        boardRolesRepository.save(boardRoles);
        boardRepository.save(board);

        me = userRepository.findById(2L).get();
        board = boardRepository.findById(2L).get();
        boardRoles = BoardRoles.builder()
                .user(me)
                .board(board)
                .boardRoles(Set.of(BoardRole.VIEWER, BoardRole.DEVELOPER, BoardRole.MANAGER))
                .build();
        board.getBoardMembers().add(me);
        boardRolesRepository.save(boardRoles);
        boardRepository.save(board);
    }

    private void createUserBoardRoles(List<User> users, Board board) {
        List<BoardRoles> boardRolesList = new ArrayList<>();
        for (User user : users) {
            if (user.getId() == 1L) {
                BoardRoles boardRoles = BoardRoles.builder()
                        .user(user)
                        .board(board)
                        .boardRoles(Set.of(BoardRole.VIEWER, BoardRole.MANAGER))
                        .build();
                boardRolesList.add(boardRoles);
                continue;
            }
            BoardRoles boardRoles = BoardRoles.builder()
                    .user(user)
                    .board(board)
                    .boardRoles(getSetOfRoles())
                    .build();
            boardRolesList.add(boardRoles);
        }
        boardRolesRepository.saveAll(boardRolesList);
    }

    private List<Task> getTasks(List<User> users, Board board) {
        List<Task> tasks = new ArrayList<>();
        List<Status> statuses = List.of(Status.NEW, Status.IN_PROGRESS, Status.DONE, Status.REVIEW);
        for (int i = 0; i < 10; i++) {
            Task task = new Task();
            task.setBoard(board);
            task.setOwner(users.get(getRandom(0, users.size() - 1)));
            task.setAssignees(getAssignes(users));
            task.setPriority(getRandomEnum(Priority.class));
            task.setStatus(statuses.get(getRandom(0, statuses.size() - 1)));
            task.setName("Task " + (i + 1));
            task.setDeadline(LocalDateTime.now().plusDays(getRandom(0, 2)));
            task.setDescription("admin task");
            task.setTag(getRandomEnum(Tag.class));
            task.setProgress(getRandom(0, 100));
            tasks.add(task);
        }
        return tasks;
    }

    private Set<BoardRole> getSetOfRoles() {
        Set<BoardRole> boardRoles = new HashSet<>();
        boardRoles.add(BoardRole.VIEWER);
        BoardRole nextBoardRole;
        do {
            nextBoardRole = getRandomEnum(BoardRole.class);
        } while (boardRoles.contains(nextBoardRole));
        boardRoles.add(nextBoardRole);
        return boardRoles;
    }

    private <T extends Enum<T>> T getRandomEnum(Class<T> enumClass) {
        T[] enumConstants = enumClass.getEnumConstants();
        return enumConstants[getRandom(0, enumConstants.length - 1)];
    }

    private Set<User> getAssignes(List<User> users) {
        Set<User> assignees = new HashSet<>();
        assignees.add(users.get(getRandom(0, users.size() - 1)));
        User nextUser;
        do {
            nextUser = users.get(getRandom(0, users.size() - 1));
        } while (assignees.contains(nextUser));
        assignees.add(nextUser);
        return assignees;
    }

    private int getRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }

    private List<User> getUsers(int count) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            User user = new User();
            user.setUsername("user" + (i + 1));
            user.setFirstName("firstname" + (i + 1));
            user.setLastName("lastname" + (i + 1));
            user.setEmail("user" + (i + 1) + "@gmail.com");
            Set<Role> roles = new HashSet<>();
            roles.add(Role.USER);
            user.setRoles(roles);
            user.setPassword(encoder.encode("123456"));
            users.add(user);
        }
        return users;
    }

    private Board getBoard(User owner, String name, String description) {
        Board board = new Board();
        board.setOwner(owner);
        board.setScope(getRandomEnum(Scope.class));
        board.setName(name);
        board.setDescription(description);
        return board;
    }
}
