package com.example.demo.service.imp;

import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import com.example.demo.model.VideoPlaylistOrder;
import com.example.demo.repo.VideoPlaylistOrderRepo;
import com.example.demo.service.VideoPlaylistOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoPlaylistOrderServiceImpl implements VideoPlaylistOrderService {

    @Autowired
    VideoPlaylistOrderRepo videoPlaylistOrderRepo;

    @Override
    public List<VideoPlaylistOrder> sortedVideoList(Playlist playlist) {



        return videoPlaylistOrderRepo.getVideoPlaylistsOrdersByPlaylist_Id(playlist.getId());
    }
}
