package com.example.demo.service;

import com.example.demo.model.Playlist;

import com.example.demo.model.Video;
import com.example.demo.model.User;

import java.util.List;

public interface PlaylistVideoService {

    Playlist findOne(String playlistId);

    List<Playlist> findAll();

    Playlist save(Playlist playlist);

    void remove(String playlistId);

    Playlist videoSort(String playlistId);

    Playlist addVideo(String playlistId, String videoId);

    Playlist removeVideo(String playlistId ,String videoId);

    Playlist videoIndex(String playlistId, Integer currentIndex, Integer newIndex);

}
