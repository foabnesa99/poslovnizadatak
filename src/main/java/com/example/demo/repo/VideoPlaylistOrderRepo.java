package com.example.demo.repo;

import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import com.example.demo.model.VideoPlaylistOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoPlaylistOrderRepo extends JpaRepository<VideoPlaylistOrder, String> {
    
    List<VideoPlaylistOrder> getVideoPlaylistsOrdersByPlaylist_Id (String playlistId);

    List<VideoPlaylistOrder> getVideoPlaylistOrdersByPlaylist(Playlist playlist);

    VideoPlaylistOrder getVideoPlaylistOrderByVideo(Video video);

    Optional<VideoPlaylistOrder> getVideoPlaylistOrderByPlaylistAndVideo(Playlist playlist, Video video);
}
