package com.example.demo.util.exceptions;

public class PlaylistMissingException extends RuntimeException{

    @Override
    public String getMessage() {
        return "The requested playlist does not exist!";
    }
}
