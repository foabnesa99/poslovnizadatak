package com.example.demo.service.imp;

import com.example.demo.model.Channel;
import com.example.demo.repo.ChannelRepo;
import com.example.demo.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelServiceImp implements ChannelService {

    @Autowired
    ChannelRepo channelRepo;

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
}
