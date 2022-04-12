package com.example.demo.service.imp;

import com.example.demo.model.Channel;
import com.example.demo.model.Playlist;
import com.example.demo.repo.ChannelRepo;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.service.ChannelService;
import com.example.demo.service.PlaylistService;
import com.example.demo.service.PlaylistVideoService;
import com.example.demo.util.exceptions.ChannelMissingException;
import com.example.demo.util.exceptions.PlaylistMissingException;
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
    PlaylistService playlistService;

    @Autowired
    PlaylistRepo playlistRepo;

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
    public Channel getChannel(String channelId) {
        Optional<Channel> channel = channelRepo.findById(channelId);
        if (channel.isEmpty()) {
            throw new PlaylistMissingException();
        }
        return channel.get();
    }

}
