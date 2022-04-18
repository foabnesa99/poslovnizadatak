package com.example.demo.service.imp;

import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import com.example.demo.model.VideoPlaylistOrder;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.repo.VideoPlaylistOrderRepo;
import com.example.demo.repo.VideoRepo;
import com.example.demo.service.PlaylistService;
import com.example.demo.service.PlaylistVideoService;
import com.example.demo.util.exceptions.ResourceMissingException;
import com.example.demo.util.exceptions.VideoMissingException;
import com.example.demo.util.exceptions.VideoNotInPlaylistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PlaylistVideoServiceImp implements PlaylistVideoService {

   private final PlaylistRepo playlistRepo;

    private final VideoPlaylistOrderRepo videoPlaylistOrderRepo;
    private final VideoRepo videoRepo;

    private final PlaylistService playlistService;

    public PlaylistVideoServiceImp(PlaylistRepo playlistRepo, VideoPlaylistOrderRepo videoPlaylistOrderRepo, VideoRepo videoRepo, PlaylistService playlistService) {
        this.playlistRepo = playlistRepo;
        this.videoPlaylistOrderRepo = videoPlaylistOrderRepo;
        this.videoRepo = videoRepo;
        this.playlistService = playlistService;
    }

    @Override
    public List<VideoPlaylistOrder> videoSort(String playlistId) {
        Playlist playlist = playlistService.getPlaylist(playlistId);

        Comparator<VideoPlaylistOrder> orderNumber = Comparator.comparing(VideoPlaylistOrder::getOrderNumber);

        List<VideoPlaylistOrder> playlistForSorting = videoPlaylistOrderRepo.getVideoPlaylistOrdersByPlaylist(playlist);

        playlistForSorting.sort(orderNumber);

        return playlistForSorting;
    }

    @Override
    public List<VideoPlaylistOrder> videoSortByName(String playlistId) {
        Playlist playlist = playlistService.getPlaylist(playlistId);
        Comparator<VideoPlaylistOrder> orderNumber = Comparator.comparing(o -> o.getVideo().getName());


        List<VideoPlaylistOrder> playlistForSorting = videoPlaylistOrderRepo.getVideoPlaylistOrdersByPlaylist(playlist);

        playlistForSorting.stream().sorted(orderNumber).collect(Collectors.toList());

        for(VideoPlaylistOrder v:playlistForSorting){
            v.setOrderNumber(playlistForSorting.indexOf(v));
        }

        return playlistForSorting;
    }

    @Override
    public Integer removeVideoFromPlaylist(String playlistId, String videoId) {

        Video video = videoRepo.getById(videoId);
        Playlist playlist = playlistRepo.getById(playlistId);

        Optional<VideoPlaylistOrder> videoPlaylistOrder = videoPlaylistOrderRepo.getVideoPlaylistOrderByPlaylistAndVideo(playlist, video);

        if(videoPlaylistOrder.isEmpty()) throw new ResourceMissingException();

        videoPlaylistOrderRepo.delete(videoPlaylistOrder.get());

        return 0;

    }




    @Override
    public List<VideoPlaylistOrder> videoIndex(String playlistId, String videoId, Integer newIndex) {


        Optional<Video> video = videoRepo.findById(videoId);

        Playlist playlist = playlistService.getPlaylist(playlistId);

        if(video.isEmpty()) throw new VideoMissingException();

        Optional<VideoPlaylistOrder> foundVideoPlaylist = videoPlaylistOrderRepo.getVideoPlaylistOrderByPlaylistAndVideo(playlist, video.get());

        if(foundVideoPlaylist.isEmpty()) {
            throw new ResourceMissingException();
        }

        List<VideoPlaylistOrder> playlists = videoPlaylistOrderRepo.getVideoPlaylistOrdersByPlaylist(playlist);

        Integer indexOfVideo = playlists.indexOf(foundVideoPlaylist.get());

        VideoPlaylistOrder foundVideo = playlists.get(indexOfVideo);

        if (newIndex > playlists.size()) {
            Integer oldIndex = foundVideo.getOrderNumber();
            foundVideo.setOrderNumber(playlists.size());
            videoPlaylistOrderRepo.save(foundVideo);
            for (VideoPlaylistOrder v : playlists) {

                if (v.getOrderNumber() > oldIndex && v.getId() != foundVideo.getId()) {
                    v.setOrderNumber(v.getOrderNumber() - 1);

                }
                videoPlaylistOrderRepo.save(v);
            }
            }

        else if(newIndex > indexOfVideo) {

            for (VideoPlaylistOrder v : playlists) {

                if (v.getOrderNumber() <= newIndex && v.getOrderNumber() >= indexOfVideo) {
                    v.setOrderNumber(v.getOrderNumber() - 1);

                }
                videoPlaylistOrderRepo.save(v);
            }
            foundVideo.setOrderNumber(newIndex);

        }
            else if(newIndex < indexOfVideo) {
                for (VideoPlaylistOrder v : playlists) {
                    if (v.getOrderNumber() >= newIndex && v.getOrderNumber() <= indexOfVideo) {
                    v.setOrderNumber(v.getOrderNumber() + 1);
                }

                videoPlaylistOrderRepo.save(v);
            }
            foundVideo.setOrderNumber(newIndex);
        }


        videoPlaylistOrderRepo.save(foundVideo);

/*
        Comparator<VideoPlaylistOrder> orderNumber = Comparator.comparing(VideoPlaylistOrder::getOrderNumber);

        playlists.sort(orderNumber);
*/

        return playlists;
    }
}
