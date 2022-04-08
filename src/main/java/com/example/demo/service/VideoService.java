package com.example.demo.service;


import com.example.demo.model.Playlist;
import com.example.demo.model.Video;

import java.util.List;

public interface VideoService {

    Video findOne(String videoId);

    List<Video> findAll();

    Video save(Video video);

    void remove(String videoId);

    Playlist addVideoToPlaylist(String videoId, String playlistId);
}
