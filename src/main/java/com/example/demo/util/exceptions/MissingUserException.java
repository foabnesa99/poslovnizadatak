package com.example.demo.util.exceptions;

public class MissingUserException extends RuntimeException{

    @Override
    public String getMessage() {
        return "The requested user does not exist!";
    }
}
