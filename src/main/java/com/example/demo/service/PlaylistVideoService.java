package com.example.demo.service;

import com.example.demo.model.Playlist;

import com.example.demo.model.Video;
import com.example.demo.model.User;
import com.example.demo.model.VideoPlaylistOrder;
import com.example.demo.model.dto.PlaylistSortedDTO;

import java.util.List;

public interface PlaylistVideoService {



    List<VideoPlaylistOrder> videoSort(String playlistId);

    List<VideoPlaylistOrder>  videoSortByName(String playlistId);

    void removeVideoFromPlaylist (String playlistId, String videoId);

    List<VideoPlaylistOrder> videoIndex(String playlistId, String videoId, Integer newIndex);

}
