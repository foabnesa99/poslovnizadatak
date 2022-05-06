package com.example.demo.service;

import com.example.demo.model.Playlist;
import com.example.demo.model.User;
import com.example.demo.model.Video;
import com.example.demo.model.VideoPlaylist;

import java.util.List;
import java.util.Set;

public interface PlaylistVideoService {


    void removeVideo(String videoId);
    List<VideoPlaylist> videoSort(String playlistId);

    List<VideoPlaylist> removeVideoFromPlaylist (String playlistId, String videoId);

    List<VideoPlaylist> videoIndex(String playlistId, String videoId, Integer newIndex);

    List<Video> videosInPlaylist(Playlist playlist);

    Set<Video> videosInUserPlaylists(User user);

}
