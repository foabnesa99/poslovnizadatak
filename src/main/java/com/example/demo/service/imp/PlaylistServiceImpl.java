package com.example.demo.service.imp;

import com.example.demo.model.Playlist;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.service.PlaylistService;
import com.example.demo.util.exceptions.PlaylistMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired
    PlaylistRepo playlistRepo;

    @Override
    public Playlist findOne(String playlistId) {
        return checkIfExists(playlistId);
    }

    @Override
    public List<Playlist> findAll() {
        return playlistRepo.findAll();
    }

    @Override
    public Playlist save(Playlist playlist) {
        return playlistRepo.save(playlist);
    }

    @Override
    public void remove(String playlistId) {

        playlistRepo.deleteById(checkIfExists(playlistId).getId());
    }

    @Override
    public Playlist checkIfExists(String playlistId) {
        Optional<Playlist> playlist = playlistRepo.findById(playlistId);
        if (playlist.isEmpty()) {
            throw new PlaylistMissingException();
        }
        return playlist.get();
    }
}
