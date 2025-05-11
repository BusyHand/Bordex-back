package com.ugrasu.bordexback.telegram.command;

import lombok.RequiredArgsConstructor;

import static java.util.Arrays.stream;

@RequiredArgsConstructor
public enum Command {

    NOT_SUPPORTED("not_supported"),

    START("/start"),

    START_REGISTRATION("/start registration");

    private final String commandText;

    public static Command of(String userText) {
        return stream(values())
                .filter(command -> command.commandText.contains(userText))
                .findFirst()
                .orElse(NOT_SUPPORTED);
    }
}
