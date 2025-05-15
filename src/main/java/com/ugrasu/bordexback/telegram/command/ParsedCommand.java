package com.ugrasu.bordexback.telegram.command;

import java.util.Map;

public record ParsedCommand(Command command, Map<Param, String> params) {
    public String getParam(Param param) {
        if (!params.containsKey(param)) {
            return "";
        }
        return params.get(param);
    }
}