package com.example.demo.service;

import com.example.demo.model.Playlist;

import com.example.demo.model.VideoPlaylistOrder;
import com.example.demo.model.dto.PlaylistSortedDTO;

import java.util.List;

public interface VideoPlaylistOrderService {

    List<VideoPlaylistOrder> sortedVideoList(Playlist playlist);

}
