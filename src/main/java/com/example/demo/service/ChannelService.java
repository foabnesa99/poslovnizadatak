package com.example.demo.service;

import com.example.demo.model.Channel;
import com.example.demo.model.Playlist;

import java.util.List;

public interface ChannelService {


    List<Channel> findAll();

    Channel save(Channel channel);

    void remove(String channelId);

    Channel getChannel(String channelId);
}
