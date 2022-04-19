package com.example.demo.service;

import com.example.demo.model.PlaylistChannel;

import java.util.List;

public interface ChannelPlaylistService {

    List<PlaylistChannel> playlistSort(String channelId);

    List<PlaylistChannel> removePlaylistFromChannel (String channelId, String playlistId);

    List<PlaylistChannel> playlistIndex(String channelId, String playlistId, Integer newIndex);

    PlaylistChannel addPlaylistToChannel(String channelId, String playlistId);
}
