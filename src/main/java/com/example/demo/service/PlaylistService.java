package com.example.demo.service;

import com.example.demo.model.Channel;
import com.example.demo.model.Playlist;

import java.util.List;

public interface PlaylistService {


    List<Playlist> findAll();

    Playlist save(Playlist playlist);

    void remove(String playlistId);

    Playlist getPlaylist(String playlistId);


}
