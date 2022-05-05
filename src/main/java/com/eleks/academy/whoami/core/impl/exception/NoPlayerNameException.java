package com.eleks.academy.whoami.core.impl.exception;

public class NoPlayerNameException extends RuntimeException {

    public NoPlayerNameException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }
}
