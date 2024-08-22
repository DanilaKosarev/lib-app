package com.example.library.exceptions;

public class BookIsAlreadyTakenException extends RuntimeException {
    public BookIsAlreadyTakenException(String message){
        super(message);
    }
}
