package com.example.demo.service.imp;

import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import com.example.demo.model.VideoPlaylist;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.repo.VideoPlaylistRepo;
import com.example.demo.repo.VideoRepo;
import com.example.demo.service.PlaylistService;
import com.example.demo.service.PlaylistVideoService;
import com.example.demo.service.VideoService;
import com.example.demo.util.exceptions.ResourceMissingException;
import com.example.demo.util.exceptions.VideoMissingException;
import com.example.demo.util.exceptions.VideoNotInPlaylistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
public class PlaylistVideoServiceImp implements PlaylistVideoService {

    private final PlaylistRepo playlistRepo;
    private final VideoService videoService;
    private final VideoPlaylistRepo videoPlaylistRepo;
    private final VideoRepo videoRepo;
    private final PlaylistService playlistService;

    public PlaylistVideoServiceImp(PlaylistRepo playlistRepo, VideoService videoService, VideoPlaylistRepo videoPlaylistRepo, VideoRepo videoRepo, PlaylistService playlistService) {
        this.playlistRepo = playlistRepo;
        this.videoService = videoService;
        this.videoPlaylistRepo = videoPlaylistRepo;
        this.videoRepo = videoRepo;
        this.playlistService = playlistService;
    }

    @Override
    public List<VideoPlaylist> videoSort(String playlistId) {
        Playlist playlist = playlistService.getPlaylist(playlistId);
        log.info("Sorting a playlist with the id {} ...", playlistId);

        return videoPlaylistRepo.getVideoPlaylistByPlaylistOrderByOrderNumber(playlist);
    }


    @Override
    public List<VideoPlaylist> removeVideoFromPlaylist(String playlistId, String videoId) {
        log.info("Removing a video with the id {} from playlist {}...", videoId, playlistId);
        Video video = videoService.getVideo(videoId);
        Playlist playlist = playlistService.getPlaylist(playlistId);

        Optional<VideoPlaylist> videoPlaylistOrder = videoPlaylistRepo.getVideoPlaylistByPlaylistAndVideo(playlist, video);

        List<VideoPlaylist> videoPlaylistList = videoPlaylistRepo.getVideoPlaylistsByPlaylist(playlist);

        if (videoPlaylistOrder.isEmpty()) throw new VideoNotInPlaylistException();

        for(VideoPlaylist v : videoPlaylistList){
            if(v.getOrderNumber() > videoPlaylistOrder.get().getOrderNumber())
            {
                v.setOrderNumber(v.getOrderNumber() - 1);
                videoPlaylistRepo.save(v);
            }
        }
        videoPlaylistList.remove(videoPlaylistOrder.get());
        log.info("Video {} removed!", videoId);
        videoPlaylistRepo.delete(videoPlaylistOrder.get());
        return videoPlaylistList;
    }
    @Override
    public List<VideoPlaylist> videoIndex(String playlistId, String videoId, Integer newIndex) {

        log.info("Moving the video {} in playlist {} to a new position - {}", videoId, playlistId, newIndex);
        Optional<Video> video = videoRepo.findById(videoId);
        Playlist playlist = playlistService.getPlaylist(playlistId);
        if (video.isEmpty()) throw new VideoMissingException();
        Optional<VideoPlaylist> foundVideoPlaylist = videoPlaylistRepo.getVideoPlaylistByPlaylistAndVideo(playlist, video.get());

        if (foundVideoPlaylist.isEmpty()) {
            throw new ResourceMissingException();
        }
        List<VideoPlaylist> playlists = videoPlaylistRepo.getVideoPlaylistsByPlaylist(playlist);
        Integer indexOfVideo = playlists.indexOf(foundVideoPlaylist.get());
        VideoPlaylist foundVideo = playlists.get(indexOfVideo);

        if (newIndex > playlists.size()) {
            log.info("The new index of the video surpasses the size of the playlist. Setting the video as the last element in the playlist...");
            Integer oldIndex = foundVideo.getOrderNumber();
            foundVideo.setOrderNumber(playlists.size());
            videoPlaylistRepo.save(foundVideo);
            for (VideoPlaylist v : playlists) {
                if (v.getOrderNumber() > oldIndex && v.getId() != foundVideo.getId()) {
                    v.setOrderNumber(v.getOrderNumber() - 1);
                }
                videoPlaylistRepo.save(v);
            }

        } else if (newIndex > indexOfVideo) {
            for (VideoPlaylist v : playlists) {
                if (v.getOrderNumber() <= newIndex && v.getOrderNumber() >= indexOfVideo) {
                    v.setOrderNumber(v.getOrderNumber() - 1);
                }
                videoPlaylistRepo.save(v);
            }
            foundVideo.setOrderNumber(newIndex);

        } else if (newIndex < indexOfVideo) {
            for (VideoPlaylist v : playlists) {
                if (v.getOrderNumber() >= newIndex && v.getOrderNumber() <= indexOfVideo) {
                    v.setOrderNumber(v.getOrderNumber() + 1);
                }
                videoPlaylistRepo.save(v);
            }
            foundVideo.setOrderNumber(newIndex);
        }
        videoPlaylistRepo.save(foundVideo);
/*
        Comparator<VideoPlaylist> orderNumber = Comparator.comparing(VideoPlaylist::getOrderNumber);
        playlists.sort(orderNumber);
*/      log.info("Video {} order number updated", videoId);
        return playlists;
    }
}
