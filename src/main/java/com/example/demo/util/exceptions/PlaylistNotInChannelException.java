package com.example.demo.util.exceptions;

public class PlaylistNotInChannelException extends RuntimeException{

    @Override
    public String getMessage() {
        return "The requested playlist isn't located in the requested channel!";
    }
}
