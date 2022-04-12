package com.example.demo.service;

import com.example.demo.model.PlaylistChannelOrder;
import com.example.demo.model.VideoPlaylistOrder;

import java.util.List;

public interface ChannelPlaylistService {

    List<PlaylistChannelOrder> playlistSort(String channelId);

    void removePlaylistFromChannel (String channelId, String playlistId);

    List<PlaylistChannelOrder> playlistIndex(String channelId, String playlistId, Integer newIndex);

    PlaylistChannelOrder addPlaylistToChannel(String channelId, String playlistId);
}
