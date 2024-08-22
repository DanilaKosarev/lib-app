package com.example.library.controllers;

import com.example.library.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException e){
        Map<String, String> errorMap = new HashMap<>();

        e.getBindingResult().getFieldErrors().forEach(error -> errorMap.put(error.getField(), error.getDefaultMessage()));

        return errorMap;
    }

    @ExceptionHandler({NoSuchBookException.class, NoSuchPersonException.class, NoSuchReviewException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotFoundException(RuntimeException e){
        return Map.of("id", e.getMessage());
    }

    @ExceptionHandler(InappropriateUserException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String, String> handleInappropriateUserException(InappropriateUserException e){
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(BookIsAlreadyTakenException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleBookIsAlreadyTakenException(BookIsAlreadyTakenException e){
        return Map.of("message", e.getMessage());
    }

    @ExceptionHandler(ReviewIsAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleReviewIsAlreadyExistsException(ReviewIsAlreadyExistsException e){
        return Map.of("message", e.getMessage());
    }
}
