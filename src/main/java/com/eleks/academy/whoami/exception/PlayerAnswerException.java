package com.eleks.academy.whoami.exception;

public class PlayerAnswerException extends RuntimeException {
    public PlayerAnswerException(String message, Throwable cause) {
        super(message, cause);
        cause.printStackTrace();
    }
}
