package com.eleks.academy.whoami.exception;

public class FailGetPlayersNameException extends RuntimeException {

    public FailGetPlayersNameException() {
    }

    public FailGetPlayersNameException(String message) {
        super(message);
    }

    public FailGetPlayersNameException(Throwable cause) {
        super(cause);
    }

    public FailGetPlayersNameException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
