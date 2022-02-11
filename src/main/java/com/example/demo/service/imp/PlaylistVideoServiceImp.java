package com.example.demo.service.imp;

import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.repo.VideoRepo;
import com.example.demo.service.PlaylistVideoService;
import com.example.demo.util.exceptions.ResourceMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlaylistVideoServiceImp implements PlaylistVideoService {

    @Autowired
    PlaylistRepo playlistRepo;

    @Autowired
    VideoRepo videoRepo;

    @Override
    public Playlist findOne(String playlistId) {
        return playlistRepo.getById(playlistId);
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
        playlistRepo.deleteById(playlistId);
    }

    @Override
    public Playlist videoSort(String playlistId) {
        Optional<Playlist> playlist = playlistRepo.findById(playlistId);
        if (playlist.isEmpty()) {
            throw new ResourceMissingException();
        }

        playlist.ifPresent(name ->
                Collections.sort(playlist.get().getVideoList(), Comparator.comparing(Video::getName))

        );

        return playlistRepo.save(playlist.get());
    }


    @Override
    public Playlist addVideo(String playlistId, String videoId) {


        Optional<Playlist> playlist = playlistRepo.findById(playlistId);
        Optional<Video> video = videoRepo.findById(videoId);
        if (playlist.isEmpty() || video.isEmpty()) {
            throw new ResourceMissingException();
        }
        playlist.get().getVideoList().add(video.get());

        //We already ensured that a playlist exists
        return playlistRepo.save(playlist.get());
    }

    @Override
    public Playlist removeVideo(String playlistId, String videoId) {
        Optional<Playlist> playlist = playlistRepo.findById(playlistId);
        Optional<Video> video = videoRepo.findById(videoId);
        if (playlist.isEmpty() || video.isEmpty()) {
            throw new ResourceMissingException();
        }

        playlist.get().getVideoList().remove(video.get());
        return playlistRepo.save(playlist.get());

    }

    @Override
    public Playlist videoIndex(String playlistId, Integer currentIndex, Integer newIndex) {
        Optional<Playlist> playlist = playlistRepo.findById(playlistId);
        Video foundVideo;


        if (playlist.isEmpty()) {
            throw new ResourceMissingException();
        }

        if (newIndex > playlist.get().getVideoList().size()) {
            throw new IndexOutOfBoundsException();
        }
        List<Video> videoList = playlist.get().getVideoList();
        foundVideo = videoList.get(currentIndex);
        videoList.remove(foundVideo);
        videoList.add(newIndex, foundVideo);
        playlist.get().setVideoList(videoList);

        return playlistRepo.save(playlist.get());
    }
}
