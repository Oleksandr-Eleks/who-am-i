package com.eleks.academy.whoami.exception;

public class ObtainPlayerNameException extends RuntimeException{

    public ObtainPlayerNameException(String text) {
        super(text);
    }

    public ObtainPlayerNameException(String text, Throwable error) {
        super(text, error);
    }
}
