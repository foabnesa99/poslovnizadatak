package com.example.demo.service;

import com.example.demo.model.VideoPlaylist;

import java.util.List;

public interface PlaylistVideoService {



    List<VideoPlaylist> videoSort(String playlistId);

    List<VideoPlaylist> removeVideoFromPlaylist (String playlistId, String videoId);

    List<VideoPlaylist> videoIndex(String playlistId, String videoId, Integer newIndex);

}
