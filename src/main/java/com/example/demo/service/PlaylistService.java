package com.example.demo.service;

import com.example.demo.model.Playlist;

import java.util.List;

public interface PlaylistService {

    Playlist findOne(String playlistId);

    List<Playlist> findAll();

    Playlist save(Playlist playlist);

    void remove(String playlistId);

    Playlist checkIfExists(String playlistId);
}
