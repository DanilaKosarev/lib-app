package com.example.library.exceptions;

public class NoSuchPersonException extends RuntimeException {
    public NoSuchPersonException (String message) {
        super(message);
    }
}
