package com.ugrasu.bordexback.rest.runner;

import com.ugrasu.bordexback.rest.entity.Board;
import com.ugrasu.bordexback.rest.entity.BoardRoles;
import com.ugrasu.bordexback.rest.entity.Task;
import com.ugrasu.bordexback.rest.entity.User;
import com.ugrasu.bordexback.rest.entity.enums.*;
import com.ugrasu.bordexback.rest.repository.BoardRepository;
import com.ugrasu.bordexback.rest.repository.BoardRolesRepository;
import com.ugrasu.bordexback.rest.repository.TaskRepository;
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

        Board board1 = getBoard(users.get(0), "Frontend", "Разработка интерфейса сайта bordex");
        Board board2 = getBoard(users.get(1), "Backend", "Разработка серверной части сайта bordex");

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

        List<Task> tasks1 = getTasks(firstUsers, savedBoard1, tasksName1, tasksDesc1);
        List<Task> tasks2 = getTasks(secondUsers, savedBoard2, tasksName2, tasksDesc2);

        taskRepository.saveAll(tasks1);
        taskRepository.saveAll(tasks2);

        savedBoard1.setProgress(taskRepository.calculateAverageProgressByBoard(savedBoard1));
        savedBoard2.setProgress(taskRepository.calculateAverageProgressByBoard(savedBoard2));

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

    private List<Task> getTasks(List<User> users, Board board, List<String> taskNames, List<String> taskDescriptions) {
        List<Task> tasks = new ArrayList<>();
        List<Status> statuses = List.of(Status.NEW, Status.IN_PROGRESS, Status.DONE, Status.REVIEW);
        for (int i = 0; i < 10; i++) {
            Task task = new Task();
            task.setBoard(board);
            task.setOwner(users.get(getRandom(0, users.size() - 1)));
            task.setAssignees(getAssignes(users));
            task.setPriority(getRandomEnum(Priority.class));
            task.setStatus(statuses.get(getRandom(0, statuses.size() - 1)));
            task.setName(taskNames.get(i));
            task.setDeadline(LocalDateTime.now().plusDays(getRandom(0, 2)));
            task.setDescription(taskDescriptions.get(i));
            task.setTag(getRandomEnum(Tag.class));
            task.setProgress(getRandom(0, 100));
            tasks.add(task);
        }
        return tasks;
    }

    private Set<BoardRole> getSetOfRoles() {
        Set<BoardRole> boardRoles = new HashSet<>();
        boardRoles.add(BoardRole.VIEWER);
        boardRoles.add(BoardRole.DEVELOPER);
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
            user.setUsername(usernames.get(i));
            user.setFirstName(firstNames.get(i));
            user.setLastName(lastNames.get(i));
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


    List<String> usernames = List.of(
            "alex", "maria", "john", "sophia", "michael",
            "luna.dev", "charlie99", "olivia_w", "ethan_thewise", "emma_k",
            "daniel42", "grace_h", "leo.strong", "isabella88", "noah_m",
            "ava.j", "liam.dev", "mia_rose", "nathan_c", "zoe_l"
    );

    List<String> firstNames = List.of(
            "Alexander", "Maria", "John", "Sophia", "Michael",
            "Luna", "Charlie", "Olivia", "Ethan", "Emma",
            "Daniel", "Grace", "Leo", "Isabella", "Noah",
            "Ava", "Liam", "Mia", "Nathan", "Zoe"
    );

    List<String> lastNames = List.of(
            "Ivanov", "Petrova", "Doe", "Smith", "Brown",
            "Taylor", "Johnson", "Williams", "Davis", "Miller",
            "Wilson", "Moore", "Anderson", "Thomas", "Jackson",
            "White", "Harris", "Martin", "Thompson", "Lee"
    );

    List<String> tasksName1 = List.of(
            "Страница входа", "Форма регистрации", "Панель пользователя", "Редактор профиля", "Панель уведомлений",
            "Поисковая строка", "Элементы пагинации", "Переключение темы", "Отображение ошибок", "Локализация интерфейса"
    );

    List<String> tasksName2 = List.of(
            "JWT-аутентификация", "Сервис пользователей", "Система ролей и прав", "Миграции базы данных", "Уведомления на почту",
            "API загрузки файлов", "Логирование и мониторинг", "Обработка исключений", "REST API", "Валидация данных"
    );

    List<String> tasksDesc1 = List.of(
            "Создание интерфейса входа с валидацией", "Реализация регистрации с проверкой полей", "Разметка панели пользователя с виджетами",
            "Редактирование профиля с предварительным просмотром", "Вывод уведомлений в реальном времени",
            "Добавление поиска с задержкой", "Реализация отображения постраничных данных", "Включение светлой и тёмной темы",
            "Показ понятных сообщений об ошибках", "Поддержка нескольких языков в интерфейсе"
    );

    List<String> tasksDesc2 = List.of(
            "Реализация аутентификации через JWT", "Бизнес-логика для работы с пользователями", "Контроль доступа по ролям и правам",
            "Управление изменениями схемы через Flyway", "Отправка уведомлений по электронной почте",
            "Создание endpoint для загрузки файлов", "Логирование запросов и сбор метрик", "Унифицированная обработка ошибок",
            "Создание REST API для CRUD операций", "Проверка входящих данных на корректность"
    );

}
