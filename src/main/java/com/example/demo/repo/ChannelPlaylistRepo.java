package com.example.demo.repo;

import com.example.demo.model.Channel;
import com.example.demo.model.Playlist;
import com.example.demo.model.PlaylistChannel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.parameters.P;

import java.util.List;
import java.util.Optional;

public interface ChannelPlaylistRepo extends JpaRepository<PlaylistChannel, String> {

    List<PlaylistChannel> getPlaylistChannelsByChannel(Channel channel);

    Optional<PlaylistChannel> getPlaylistChannelByChannelAndPlaylist (Channel channel, Playlist playlist);
    List<PlaylistChannel> getPlaylistChannelsByChannelAndPlaylist(Channel channel, Playlist playlist);

    List<PlaylistChannel> getPlaylistChannelsByChannelOrderByOrderNumber(Channel channel);

    List<PlaylistChannel> getPlaylistChannelsByPlaylist(Playlist playlist);

}
