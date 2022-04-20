package com.example.demo.util;

import com.example.demo.util.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.Date;

@RestControllerAdvice
@Slf4j
public class LogExceptionHandler {

    @ExceptionHandler(PlaylistMissingException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorHandling PlaylistNotFound(PlaylistMissingException exception,
                                              HttpServletRequest request) {

        log.error("The requested playlist has not been found", exception);
        return ErrorHandling.builder().timestamp(new Timestamp(new Date().getTime())).message(exception.getMessage()).path(request.getServletPath()).build();
    }

    @ExceptionHandler(ChannelMissingException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorHandling ChannelNotFound(ChannelMissingException exception,
                                          HttpServletRequest request) {

        log.error("The requested channel has not been found", exception);
        return ErrorHandling.builder().timestamp(new Timestamp(new Date().getTime())).message(exception.getMessage()).path(request.getServletPath()).build();
    }

    @ExceptionHandler(VideoMissingException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorHandling VideoNotFound(VideoMissingException exception,
                                          HttpServletRequest request) {

        log.error("The requested video has not been found", exception);
        return ErrorHandling.builder().timestamp(new Timestamp(new Date().getTime())).message(exception.getMessage()).path(request.getServletPath()).build();
    }

    @ExceptionHandler(PlaylistNotInChannelException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorHandling PlaylistNotInChannel(PlaylistNotInChannelException exception,
                                       HttpServletRequest request) {

        log.error("The requested playlist isn't located in the channel", exception);
        return ErrorHandling.builder().timestamp(new Timestamp(new Date().getTime())).message(exception.getMessage()).path(request.getServletPath()).build();
    }

    @ExceptionHandler(VideoNotInPlaylistException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorHandling VideoNotInPlaylist(VideoNotInPlaylistException exception,
                                              HttpServletRequest request) {

        log.error("The requested video isn't located in the playlist", exception);
        return ErrorHandling.builder().timestamp(new Timestamp(new Date().getTime())).message(exception.getMessage()).path(request.getServletPath()).build();
    }
}
