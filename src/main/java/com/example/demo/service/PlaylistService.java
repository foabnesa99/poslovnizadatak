package com.example.demo.service;

import com.example.demo.model.Playlist;

import com.example.demo.model.Video;
import com.example.demo.model.User;

import java.util.List;

public interface PlaylistService {

    Playlist findOne(String playlistId);

    List<Playlist> findAll();

    Playlist save(Playlist playlist);

    void remove(String playlistId);

    Playlist VideoSort(String playlistId);

    Playlist AddVideo(String playlistId, String videoId);

    Playlist RemoveVideo(String playlistId ,String videoId);

    Playlist VideoIndex(String playlistId, Integer currentIndex, Integer newIndex);

}
