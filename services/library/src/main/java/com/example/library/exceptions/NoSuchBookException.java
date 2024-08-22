package com.example.library.exceptions;

public class NoSuchBookException extends RuntimeException {
    public NoSuchBookException(String message) {
        super(message);
    }
}
