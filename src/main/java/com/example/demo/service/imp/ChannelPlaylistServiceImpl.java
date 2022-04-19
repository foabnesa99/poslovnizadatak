package com.example.demo.service.imp;

import com.example.demo.model.*;
import com.example.demo.repo.ChannelPlaylistRepo;
import com.example.demo.service.ChannelPlaylistService;
import com.example.demo.service.ChannelService;
import com.example.demo.service.PlaylistService;
import com.example.demo.util.exceptions.PlaylistNotInChannelException;
import com.example.demo.util.exceptions.ResourceMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChannelPlaylistServiceImpl implements ChannelPlaylistService {

    @Autowired
    ChannelService channelService;

    @Autowired
    ChannelPlaylistRepo channelPlaylistRepo;

    @Autowired
    PlaylistService playlistService;

    @Override
    public List<PlaylistChannel> playlistSort(String channelId) {
        Channel channel = channelService.getChannel(channelId);

        return channelPlaylistRepo.getPlaylistChannelByChannelOrderByOrderNumber(channel);
    }

    @Override
    public List<PlaylistChannel> removePlaylistFromChannel(String channelId, String playlistId) {

        Playlist playlist = playlistService.getPlaylist(playlistId);

        Channel channel = channelService.getChannel(channelId);

        Optional<PlaylistChannel> playlistChannelOrder = channelPlaylistRepo.getPlaylistChannelByChannelAndPlaylist(channel, playlist);

        List<PlaylistChannel> playlistChannelList = channelPlaylistRepo.getPlaylistChannelsByChannel(channel);

        if (playlistChannelOrder.isEmpty()) throw new ResourceMissingException();

        for (PlaylistChannel p : playlistChannelList) {
            if (p.getOrderNumber() > playlistChannelOrder.get().getOrderNumber()) {
                p.setOrderNumber(p.getOrderNumber() - 1);
                channelPlaylistRepo.save(p);
            }
        }

        channelPlaylistRepo.delete(playlistChannelOrder.get());
        return channelPlaylistRepo.getPlaylistChannelsByChannel(channel);
        }

    @Override
    public List<PlaylistChannel> playlistIndex(String channelId, String playlistId, Integer newIndex) {
        Channel channel = channelService.getChannel(channelId);
        Playlist playlist = playlistService.getPlaylist(playlistId);
        Optional<PlaylistChannel> foundPlaylist = channelPlaylistRepo.getPlaylistChannelByChannelAndPlaylist(channel, playlist);

        if (foundPlaylist.isEmpty()) {
            throw new PlaylistNotInChannelException();
        }

        List<PlaylistChannel> channelOrderList = channelPlaylistRepo.getPlaylistChannelsByChannel(channel);
        Integer indexOfPlaylist = channelOrderList.indexOf(foundPlaylist.get());
        PlaylistChannel playlistInChannel = channelOrderList.get(indexOfPlaylist);

        if (newIndex > channelOrderList.size()) {
            Integer oldIndex = foundPlaylist.get().getOrderNumber();
            playlistInChannel.setOrderNumber(channelOrderList.size());
            channelPlaylistRepo.save(playlistInChannel);

            for (PlaylistChannel p : channelOrderList) {
                if (p.getOrderNumber() > oldIndex && p.getId() != playlistInChannel.getId()) {
                    p.setOrderNumber(p.getOrderNumber() - 1);
                }
                channelPlaylistRepo.save(p);
            }
        } else if (newIndex > indexOfPlaylist) {
            for (PlaylistChannel p : channelOrderList) {
                if (p.getOrderNumber() <= newIndex && p.getOrderNumber() >= indexOfPlaylist) {
                    p.setOrderNumber(p.getOrderNumber() - 1);

                }
                channelPlaylistRepo.save(p);
            }
            playlistInChannel.setOrderNumber(newIndex);
            channelPlaylistRepo.save(playlistInChannel);
        } else if (newIndex < indexOfPlaylist) {
            for (PlaylistChannel p : channelOrderList) {
                if (p.getOrderNumber() >= newIndex && p.getOrderNumber() <= indexOfPlaylist) {
                    p.setOrderNumber(p.getOrderNumber() + 1);
                }

                channelPlaylistRepo.save(p);
            }
            playlistInChannel.setOrderNumber(newIndex);
            channelPlaylistRepo.save(playlistInChannel);
        }
        return channelOrderList;
    }

    @Override
    public PlaylistChannel addPlaylistToChannel(String channelId, String playlistId) {
        Playlist playlist = playlistService.getPlaylist(playlistId);

        Channel channel = channelService.getChannel(channelId);

        PlaylistChannel playlistChannel = new PlaylistChannel();

        playlistChannel.setChannel(channel);

        playlistChannel.setPlaylist(playlist);

        List<PlaylistChannel> allPlaylistsInChannel = channelPlaylistRepo.getPlaylistChannelsByChannel(channel);

        if (allPlaylistsInChannel.isEmpty()) {
            playlistChannel.setOrderNumber(1);
        } else {
            playlistChannel.setOrderNumber(allPlaylistsInChannel.get(allPlaylistsInChannel.size() - 1).getOrderNumber() + 1);
        }
        channelPlaylistRepo.save(playlistChannel);
        return playlistChannel;
    }
}
