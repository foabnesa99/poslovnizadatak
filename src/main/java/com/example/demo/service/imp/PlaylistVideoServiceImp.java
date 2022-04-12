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

    @Autowired
    PlaylistRepo playlistRepo;

    @Autowired
    VideoPlaylistOrderRepo videoPlaylistOrderRepo;

    @Autowired
    VideoRepo videoRepo;

    @Autowired
    PlaylistService playlistService;



    @Override
    public List<VideoPlaylistOrder> videoSort(String playlistId) {
        Playlist playlist = playlistService.getPlaylist(playlistId);

        Comparator<VideoPlaylistOrder> orderNumber = Comparator.comparing(VideoPlaylistOrder::getOrderNumber);

        List<VideoPlaylistOrder> playlistForSorting = videoPlaylistOrderRepo.getVideoPlaylistOrdersByPlaylist(playlist);

        playlistForSorting.stream().sorted(orderNumber).collect(Collectors.toList());

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
    public void removeVideoFromPlaylist(String playlistId, String videoId) {

        Video video = videoRepo.getById(videoId);
        Playlist playlist = playlistRepo.getById(playlistId);

        Optional<VideoPlaylistOrder> videoPlaylistOrder = videoPlaylistOrderRepo.getVideoPlaylistOrderByPlaylistAndVideo(playlist, video);

        if(videoPlaylistOrder.isEmpty()) throw new ResourceMissingException();

        videoPlaylistOrderRepo.delete(videoPlaylistOrder.get());

    }




    @Override
    public List<VideoPlaylistOrder> videoIndex(String playlistId, String videoId, Integer newIndex) {


        Optional<Video> video = videoRepo.findById(videoId);

        Playlist playlist = playlistService.getPlaylist(playlistId);

        if(video.isEmpty()) throw new VideoMissingException();

        Optional<VideoPlaylistOrder> foundVideoPlaylist = videoPlaylistOrderRepo.getVideoPlaylistOrderByPlaylistAndVideo(playlist, video.get());

        if(foundVideoPlaylist.isEmpty()){
            throw new VideoNotInPlaylistException();
        }

        List<VideoPlaylistOrder> playlists = videoPlaylistOrderRepo.getVideoPlaylistOrdersByPlaylist(playlist);

        Integer indexOfVideo = playlists.indexOf(foundVideoPlaylist);

        VideoPlaylistOrder foundVideo = playlists.get(indexOfVideo);

        if (newIndex > playlists.size()) {
            foundVideo.setOrderNumber(playlists.size());
            videoPlaylistOrderRepo.save(foundVideo);
            }

        if(newIndex > indexOfVideo) {

            for (VideoPlaylistOrder v : playlists) {

                if (v.getOrderNumber() <= newIndex && v.getOrderNumber() >= indexOfVideo) {
                    v.setOrderNumber(v.getOrderNumber() - 1);

                }
                videoPlaylistOrderRepo.save(v);
            }
            foundVideo.setOrderNumber(newIndex);

        }
            if(newIndex < indexOfVideo) {
                for (VideoPlaylistOrder v : playlists) {
                    if (v.getOrderNumber() >= newIndex && v.getOrderNumber() <= indexOfVideo) {
                    v.setOrderNumber(v.getOrderNumber() + 1);
                }

                videoPlaylistOrderRepo.save(v);
            }
            foundVideo.setOrderNumber(newIndex);
        }


        videoPlaylistOrderRepo.save(foundVideo);

        Comparator<VideoPlaylistOrder> orderNumber = Comparator.comparing(VideoPlaylistOrder::getOrderNumber);

        playlists.stream().sorted(orderNumber).collect(Collectors.toList());



        return playlists;
    }
}
