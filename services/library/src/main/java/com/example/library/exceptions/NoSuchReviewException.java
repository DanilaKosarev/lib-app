package com.example.library.exceptions;

public class NoSuchReviewException extends RuntimeException {
    public NoSuchReviewException(String message){
        super(message);
    }
}
