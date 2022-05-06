package com.example.demo.util.exceptions;

public class VideoNotInPlaylistException extends RuntimeException{

    @Override
    public String getMessage() {
        return "The requested video is not located in the playlist!";
    }
}
