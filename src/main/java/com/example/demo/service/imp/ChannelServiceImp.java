package com.example.demo.service.imp;

import com.example.demo.model.Channel;
import com.example.demo.model.Playlist;
import com.example.demo.model.User;
import com.example.demo.repo.ChannelRepo;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.service.ChannelService;
import com.example.demo.service.PlaylistService;
import com.example.demo.service.PlaylistVideoService;
import com.example.demo.util.exceptions.ChannelMissingException;
import com.example.demo.util.exceptions.PlaylistMissingException;
import com.example.demo.util.exceptions.PlaylistNotInChannelException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ChannelServiceImp implements ChannelService {

    @Autowired
    ChannelRepo channelRepo;

    @Autowired
    PlaylistService playlistService;

    @Autowired
    PlaylistRepo playlistRepo;

    @Override
    public List<Channel> findAll() {
        log.info("Retrieving a list of all channels...");
        return channelRepo.findAll();
    }

    @Override
    public Channel save(Channel channel) {
        log.info("Saving the channel to the database...");
        return channelRepo.save(channel);
    }

    @Override
    public void remove(String channelId) {
        log.info("Removing the channel from the database...");
        channelRepo.deleteById(channelId);
    }

    @Override
    public Channel getChannel(String channelId) {
        log.info("Retrieving a channel with the requested id {}", channelId);
        Optional<Channel> channel = channelRepo.findById(channelId);
        if (channel.isEmpty()) {
            throw new PlaylistMissingException();
        }
        return channel.get();
    }

    @Override
    public Channel getChannelByUser(User user) {
        Optional<Channel> channel = channelRepo.getChannelByUser(user);
        if(channel.isEmpty()){
            throw new ChannelMissingException();
        }
        return channel.get();
    }

}
