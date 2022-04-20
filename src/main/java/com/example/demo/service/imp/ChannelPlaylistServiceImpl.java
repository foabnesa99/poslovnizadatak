package com.example.demo.service.imp;

import com.example.demo.model.Channel;
import com.example.demo.model.Playlist;
import com.example.demo.model.PlaylistChannel;
import com.example.demo.repo.ChannelPlaylistRepo;
import com.example.demo.service.ChannelPlaylistService;
import com.example.demo.service.ChannelService;
import com.example.demo.service.PlaylistService;
import com.example.demo.util.exceptions.PlaylistNotInChannelException;
import com.example.demo.util.exceptions.ResourceMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
public class ChannelPlaylistServiceImpl implements ChannelPlaylistService {

    @Autowired
    ChannelService channelService;

    @Autowired
    ChannelPlaylistRepo channelPlaylistRepo;

    @Autowired
    PlaylistService playlistService;

    @Override
    public List<PlaylistChannel> playlistSort(String channelId) {
        log.info("Sorting a channel with the id {} ...", channelId);

        Channel channel = channelService.getChannel(channelId);

        return channelPlaylistRepo.getPlaylistChannelByChannelOrderByOrderNumber(channel);
    }

    @Override
    public List<PlaylistChannel> removePlaylistFromChannel(String channelId, String playlistId) {

        log.info("Removing a playlist with the id {} from channel {}...", playlistId, channelId);

        Playlist playlist = playlistService.getPlaylist(playlistId);

        Channel channel = channelService.getChannel(channelId);

        Optional<PlaylistChannel> playlistChannelOrder = channelPlaylistRepo.getPlaylistChannelByChannelAndPlaylist(channel, playlist);

        List<PlaylistChannel> playlistChannelList = channelPlaylistRepo.getPlaylistChannelsByChannel(channel);

        if (playlistChannelOrder.isEmpty()) throw new PlaylistNotInChannelException();

        for (PlaylistChannel p : playlistChannelList) {
            if (p.getOrderNumber() > playlistChannelOrder.get().getOrderNumber()) {
                p.setOrderNumber(p.getOrderNumber() - 1);
                channelPlaylistRepo.save(p);
            }
        }

        channelPlaylistRepo.delete(playlistChannelOrder.get());
        log.info("Playlist {} removed!", playlistId);
        return channelPlaylistRepo.getPlaylistChannelsByChannel(channel);
        }

    @Override
    public List<PlaylistChannel> playlistIndex(String channelId, String playlistId, Integer newIndex) {

        log.info("Moving the playlist {} in channel {} to a new position - {}", playlistId, channelId, newIndex);

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
            log.info("The new index of the playlist surpasses the size of the channel. Setting the playlist as the last element in the channel...");
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
        log.info("Playlist {} order number updated", playlistId);
        return channelOrderList;
    }

    @Override
    public PlaylistChannel addPlaylistToChannel(String channelId, String playlistId) {
        log.info("Adding a playlist with the ID {} to the channel...", playlistId);

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
        log.info("Playlist added to the channel!");
        return playlistChannel;
    }
}
