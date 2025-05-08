package com.ugrasu.bordexback.rest.exception;

public class UserNotBoardMemberException extends RuntimeException {
    public UserNotBoardMemberException(String message) {
        super(message);
    }
}
