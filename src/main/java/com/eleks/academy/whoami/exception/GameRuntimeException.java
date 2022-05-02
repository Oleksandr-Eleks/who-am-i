package com.eleks.academy.whoami.exception;

public class GameRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 6369637968294167157L;

    public GameRuntimeException(String message) {
        super(message);
    }
    
    public GameRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }
    
}