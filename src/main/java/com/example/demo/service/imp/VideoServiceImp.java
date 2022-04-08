package com.example.demo.service.imp;

import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import com.example.demo.model.VideoPlaylistOrder;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.repo.VideoPlaylistOrderRepo;
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
    VideoPlaylistOrderRepo videoPlaylistOrderRepo;

    @Autowired
    VideoRepo videoRepo;

    @Autowired
    PlaylistRepo playlistRepo;

    @Override
    public Video findOne(String videoId) {
        return videoRepo.getById(videoId);
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

        VideoPlaylistOrder videoPlaylistOrder = new VideoPlaylistOrder();
        Optional<Playlist> playlist = playlistRepo.findById(playlistId);
        if(playlist.isPresent()) {

            videoPlaylistOrder.setPlaylist(playlist.get());
        }
        else throw new PlaylistMissingException();

        Optional<Video> video = videoRepo.findById(videoId);

        if(video.isPresent()) {

            videoPlaylistOrder.setVideo(video.get());
        }
       else throw new VideoMissingException();

        List<VideoPlaylistOrder> allVideosInPlaylists = videoPlaylistOrderRepo.getVideoPlaylistOrdersByPlaylist(playlist.get());

        if(allVideosInPlaylists.isEmpty()){
            videoPlaylistOrder.setOrderNumber(1);
        }
        else{
            videoPlaylistOrder.setOrderNumber(allVideosInPlaylists.get(allVideosInPlaylists.size() - 1).getOrderNumber() + 1);
        }
        videoPlaylistOrderRepo.save(videoPlaylistOrder);
        return playlist.get();
    }
}
