package com.example.demo.util.exceptions;

public class VideoMissingException extends RuntimeException{

    @Override
    public String getMessage() {
        return "The requested video does not exist!";
    }
}
