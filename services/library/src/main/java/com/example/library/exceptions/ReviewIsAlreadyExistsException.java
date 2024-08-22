package com.example.library.exceptions;

public class ReviewIsAlreadyExistsException extends RuntimeException {
    public ReviewIsAlreadyExistsException(String message){
        super(message);
    }
}
