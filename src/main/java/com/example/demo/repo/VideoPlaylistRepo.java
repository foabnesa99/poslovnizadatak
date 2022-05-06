package com.example.demo.repo;

import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import com.example.demo.model.VideoPlaylist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoPlaylistRepo extends JpaRepository<VideoPlaylist, String> {
    
    List<VideoPlaylist> getVideoPlaylistsByPlaylist_Id (String playlistId);

    List<VideoPlaylist> getVideoPlaylistsByPlaylist(Playlist playlist);

    List<VideoPlaylist> getVideoPlaylistsByVideo(Video video);

    Optional<VideoPlaylist> getVideoPlaylistByPlaylistAndVideo(Playlist playlist, Video video);

    List<VideoPlaylist> getVideoPlaylistsByPlaylistAndVideo(Playlist playlist, Video video);

    List<VideoPlaylist> getVideoPlaylistsByPlaylistOrderByOrderNumber(Playlist playlist);
}
