package com.example.demo.service.imp;

import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import com.example.demo.model.VideoPlaylist;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.repo.VideoPlaylistRepo;
import com.example.demo.repo.VideoRepo;
import com.example.demo.service.VideoService;
import com.example.demo.util.exceptions.PlaylistMissingException;
import com.example.demo.util.exceptions.VideoMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoServiceImp implements VideoService {

    @Autowired
    VideoPlaylistRepo videoPlaylistRepo;

    @Autowired
    VideoRepo videoRepo;

    @Autowired
    PlaylistRepo playlistRepo;

    @Override
    public Video findOne(String videoId) {
        return videoRepo.getById(videoId);
    }

    @Override
    public Video getVideo(String videoId) {
        Optional<Video> video = videoRepo.findById(videoId);
        if(video.isEmpty()) throw new VideoMissingException();
        return video.get();
    }

    @Override
    public List<Video> findAll() {
        return videoRepo.findAll();
    }

    @Override
    public Video save(Video video) {
        return videoRepo.save(video);
    }

    @Override
    public void remove(String videoId) {
        videoRepo.deleteById(videoId);
    }

    @Override
    public Playlist addVideoToPlaylist(String videoId, String playlistId) {

        VideoPlaylist videoPlaylist = new VideoPlaylist();
        Optional<Playlist> playlist = playlistRepo.findById(playlistId);
        if(playlist.isPresent()) {

            videoPlaylist.setPlaylist(playlist.get());
        }
        else throw new PlaylistMissingException();

        Optional<Video> video = videoRepo.findById(videoId);

        if(video.isPresent()) {

            videoPlaylist.setVideo(video.get());
        }
       else throw new VideoMissingException();

        List<VideoPlaylist> allVideosInPlaylists = videoPlaylistRepo.getVideoPlaylistsByPlaylist(playlist.get());

        if(allVideosInPlaylists.isEmpty()){
            videoPlaylist.setOrderNumber(1);
        }
        else{
            videoPlaylist.setOrderNumber(allVideosInPlaylists.get(allVideosInPlaylists.size() - 1).getOrderNumber() + 1);
        }
        videoPlaylistRepo.save(videoPlaylist);
        return playlist.get();
    }
}
