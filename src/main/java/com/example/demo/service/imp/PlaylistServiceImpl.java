package com.example.demo.service.imp;

import com.example.demo.model.Playlist;
import com.example.demo.model.PlaylistChannel;
import com.example.demo.repo.ChannelPlaylistRepo;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.service.ChannelPlaylistService;
import com.example.demo.service.PlaylistService;
import com.example.demo.util.exceptions.PlaylistMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired
    PlaylistRepo playlistRepo;

    @Autowired
    ChannelPlaylistRepo channelPlaylistRepo;

    @Override
    public List<Playlist> findAll() {
        log.info("Retrieving a list of all playlists...");
        return playlistRepo.findAll();
    }

    @Override
    public Playlist save(Playlist playlist) {
        log.info("Saving the playlist to the database...");
        return playlistRepo.save(playlist);
    }

    @Override
    public void remove(String playlistId) {
        log.info("Removing the playlist from the database...");
        playlistRepo.deleteById(playlistId);
    }

    @Override
    public Playlist getPlaylist(String playlistId) {
        log.info("Retrieving a playlist with the requested id {}", playlistId);
        Optional<Playlist> playlist = playlistRepo.findById(playlistId);
        if (playlist.isEmpty()) {
            throw new PlaylistMissingException();
        }
        return playlist.get();
    }
}
