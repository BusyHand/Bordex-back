package com.ugrasu.bordexback.telegram.command;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.lang.Integer.compare;
import static java.lang.Math.min;
import static java.util.Arrays.stream;
import static java.util.stream.IntStream.range;

@Getter
public enum Command {
    NOT_SUPPORTED("not_supported"),

    START("/start"),

    START_REGISTRATION("/start registration"),

    START_ASSIGN_ACCOUNT("/start assign_account_", Param.TOKEN);

    private final String commandText;
    private final List<Param> params;

    Command(String commandText, Param... params) {
        this.commandText = commandText;
        this.params = Arrays.asList(params);
    }

    public static ParsedCommand of(String inputText) {
        return stream(values())
                .filter(cmd -> inputText.startsWith(cmd.commandText))
                .min((a, b) -> compare(b.commandText.length(), a.commandText.length()))
                .map(cmd -> getParsedCommand(inputText, cmd))
                .orElse(new ParsedCommand(NOT_SUPPORTED, Collections.emptyMap()));
    }

    private static @NotNull ParsedCommand getParsedCommand(String inputText, Command cmd) {
        String argsPart = inputText.substring(cmd.commandText.length()).trim();
        String[] args = argsPart.isEmpty() ? new String[0] : argsPart.split("_+");
        Map<Param, String> paramMap = range(0, min(args.length, cmd.params.size()))
                .boxed()
                .collect(Collectors.toMap(
                        i -> cmd.params.get(i),
                        i -> args[i]
                ));
        return new ParsedCommand(cmd, paramMap);
    }

}

