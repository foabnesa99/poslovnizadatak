package com.example.demo.repo;

import com.example.demo.model.Playlist;
import com.example.demo.model.VideoPlaylistOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VideoPlaylistOrderRepo extends JpaRepository<VideoPlaylistOrder, String> {
    
    List<VideoPlaylistOrder> getVideoPlaylistsOrderByPlaylist_Id (String playlistId);
}
