package com.example.demo.service.imp;

import com.example.demo.model.Channel;
import com.example.demo.model.Playlist;
import com.example.demo.repo.ChannelRepo;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.service.ChannelService;
import com.example.demo.service.PlaylistVideoService;
import com.example.demo.util.exceptions.ChannelMissingException;
import com.example.demo.util.exceptions.PlaylistNotInChannelException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChannelServiceImp implements ChannelService {

    @Autowired
    ChannelRepo channelRepo;

    @Autowired
    PlaylistVideoService playlistVideoService;

    @Autowired
    PlaylistRepo playlistRepo;

    @Override
    public Channel findOne(String channelId) {
        return channelRepo.getById(channelId);
    }

    @Override
    public List<Channel> findAll() {
        return channelRepo.findAll();
    }

    @Override
    public Channel save(Channel channel) {
        return channelRepo.save(channel);
    }

    @Override
    public void remove(String channelId) {
        channelRepo.deleteById(channelId);
    }

    @Override
    public Channel addPlaylist(String channelId, String playlistId) {
        Optional<Channel> channel = channelRepo.findById(channelId);

        Playlist playlist = playlistVideoService.checkIfExists(playlistId);

        if(channel.isEmpty()) throw new ChannelMissingException();

        channel.get().getPlaylistList().add(playlist);

        playlist.setChannel(channel.get());

        playlistRepo.save(playlist);

        return channelRepo.save(channel.get());
    }

    @Override
    public Channel removePlaylist(String channelId, String playlistId) {
        Optional<Channel> channel = channelRepo.findById(channelId);

        Playlist playlist = playlistVideoService.checkIfExists(playlistId);

        if(channel.isEmpty()) throw new ChannelMissingException();

        Integer playlistIndex = channel.get().getPlaylistList().indexOf(playlist);

        if(playlistIndex == -1) throw new PlaylistNotInChannelException();

        channel.get().getPlaylistList().remove(channel.get().getPlaylistList().get(playlistIndex));

        return channel.get();
    }
}
