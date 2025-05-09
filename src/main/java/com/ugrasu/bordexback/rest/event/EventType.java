package com.ugrasu.bordexback.rest.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {

    // TASK
    TASK_CREATED("Задача создана", "Создана задача \"%s\" на доске \"%s\""),
    TASK_UPDATED("Задача обновлена", "Обновлена задача \"%s\" на доске \"%s\""),
    TASK_DELETED("Задача удалена", "Удалена задача \"%s\" с доски \"%s\""),
    TASK_ASSIGNED("Задача назначена", "Задача \"%s\" на доске \"%s\" назначена пользователю \"%s\""),
    TASK_UNASSIGNED("Задача снята", "С задачи \"%s\" на доске \"%s\" был снят пользователь \"s%\""),


    // BOARD
    BOARD_CREATED("Доска создана", "Создана доска \"%s\""),
    BOARD_UPDATED("Доска обновлена", "Обновлена доска \"%s\""),
    BOARD_DELETED("Доска удалена", "Удалена доска \"%s\""),
    BOARD_ASSIGNED("Пользователь добавлен к доске", "Добавлен новый пользователь в доску \"%s\""),
    BOARD_UNASSIGNED("Пользователь удалён с доски", "Удален пользователь с доски \"%s\""),
    BOARD_OWNER_CHANGED("Владелец доски изменён", "Владелец доски \"%s\" изменён"),

    // BOARD ROLE
    BOARD_ROLE_CREATED("Роль на доске создана", "Назначена роль \"%s\" пользователю \"%s\" в доске \"%s\""),
    BOARD_ROLE_UPDATED("Роль на доске обновлена", "Обновлена роль пользователя \"%s\" на \"%s\" в доске \"%s\""),
    BOARD_ROLE_DELETED("Роль на доске удалена", "Удалена роль \"%s\" у пользователя \"%s\" в доске \"%s\""),
    BOARD_ROLES_DELETED("Роли на доске удалены", "Удалены все роли пользователя \"%s\" в доске \"%s\""),

    // USER
    USER_CREATE("Пользователь создан", "Создан пользователь \"%s\""),
    USER_UPDATE("Пользователь обновлён", "Обновлены данные пользователя \"%s\""),
    USER_DELETED("Пользователь удалён", "Удалён пользователь \"%s\""),

    // NOTIFICATION
    NOTIFICATION_CREATE("Уведомление создано", "%s");

    private final String title;
    private final String contentTemplate;

    public String formatContent(Object... args) {
        return String.format(contentTemplate, args);
    }
}
