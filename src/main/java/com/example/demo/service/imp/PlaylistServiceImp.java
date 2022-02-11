package com.example.demo.service.imp;

import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.repo.VideoRepo;
import com.example.demo.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class PlaylistServiceImp implements PlaylistService {

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
    public Playlist VideoSort(String playlistId) {
        Optional<Playlist> playlist = playlistRepo.findById(playlistId);
        System.out.println(playlist.get().getVideoList().toString());
        playlist.ifPresent(name ->
                Collections.sort(playlist.get().getVideoList(), Comparator.comparing(Video::getName))

        );
        System.out.println(playlist.get().getVideoList().toString());
        playlistRepo.save(playlist.get());
       return playlist.orElseThrow(NullPointerException::new);
    }



    @Override
    public Playlist AddVideo(String playlistId, String videoId){
        Optional<Playlist> playlist = playlistRepo.findById(playlistId);
        Optional<Video> video = videoRepo.findById(videoId);
        playlist.get().getVideoList().add(video.get());
        playlistRepo.save(playlist.get());
        return playlist.orElseThrow(NullPointerException::new);
    }

    @Override
    public Playlist RemoveVideo(String playlistId, String videoId) {
        Optional<Playlist> playlist = playlistRepo.findById(playlistId);
        Optional<Video> video = videoRepo.findById(videoId);
        playlist.get().getVideoList().remove(video.get());
        System.out.println(video.toString());
        System.out.println(playlist.get().getVideoList().toString());
        playlistRepo.save(playlist.get());
        return playlist.orElseThrow(NullPointerException::new);
    }

    @Override
    public Playlist VideoIndex(String playlistId, Integer currentIndex, Integer newIndex) {
        Playlist playlist = playlistRepo.getById(playlistId);
        List<Video> videoList = playlist.getVideoList();
        Video foundVideo;
        foundVideo = videoList.get(currentIndex);
        System.out.println(foundVideo.toString());
        videoList.remove(foundVideo);
        System.out.println(videoList.toString());
        videoList.add(newIndex,foundVideo);

        playlist.setVideoList(videoList);
        playlistRepo.save(playlist);
        return playlist;
    }
}
