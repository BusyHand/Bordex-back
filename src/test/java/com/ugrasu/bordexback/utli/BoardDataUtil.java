package com.ugrasu.bordexback.utli;

import com.ugrasu.bordexback.entity.Board;
import com.ugrasu.bordexback.entity.User;
import com.ugrasu.bordexback.entity.enums.Scope;
import lombok.experimental.UtilityClass;

@UtilityClass
public class BoardDataUtil {


    public Board getSimpleBoard() {
        return Board.builder()
                .name("Test board")
                .description("Test description board")
                .scope(Scope.PUBLIC)
                .build();
    }
}
