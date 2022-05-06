package com.example.demo.service;

import com.example.demo.model.Channel;
import com.example.demo.model.Playlist;
import com.example.demo.model.PlaylistChannel;
import com.example.demo.model.User;

import java.util.List;

public interface ChannelPlaylistService {

    List<PlaylistChannel> getPlaylistChannelByPlaylist(Playlist playlist);

    List<PlaylistChannel> playlistSort(String channelId);

    List<PlaylistChannel> removePlaylistFromChannel (String channelId, String playlistId);

    List<PlaylistChannel> playlistIndex(String channelId, String playlistId, Integer newIndex);

    PlaylistChannel addPlaylistToChannel(String channelId, String playlistId);

    List<Playlist> findPlaylistsForUser(User user);

    void deletePlaylist(String playlistId);

    void deleteChannel(String channelId);
}
