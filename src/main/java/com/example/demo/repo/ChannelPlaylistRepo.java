package com.example.demo.repo;

import com.example.demo.model.Channel;
import com.example.demo.model.Playlist;
import com.example.demo.model.PlaylistChannelOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelPlaylistRepo extends JpaRepository<PlaylistChannelOrder, String> {

    List<PlaylistChannelOrder> getPlaylistChannelOrderByChannel(Channel channel);

    Optional<PlaylistChannelOrder> getPlaylistChannelOrderByChannelAndPlaylist (Channel channel, Playlist playlist);

    List<PlaylistChannelOrder> getPlaylistChannelOrdersByChannelAndPlaylist(Channel channel, Playlist playlist);
}
