package com.example.library.exceptions;

public class InappropriateUserException extends RuntimeException{
    public InappropriateUserException(String message){
        super(message);
    }
}
