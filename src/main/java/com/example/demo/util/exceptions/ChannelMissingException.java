package com.example.demo.util.exceptions;

public class ChannelMissingException extends RuntimeException{
    @Override
    public String getMessage() {
        return "The requested channel does not exist!";
    }
}
