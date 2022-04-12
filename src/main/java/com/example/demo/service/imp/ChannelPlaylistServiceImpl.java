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
    public List<PlaylistChannelOrder> playlistSort(String channelId) {
        Channel channel = channelService.getChannel(channelId);

        Comparator<PlaylistChannelOrder> orderNumber = Comparator.comparing(PlaylistChannelOrder::getOrderNumber);

        List<PlaylistChannelOrder> playlistForSorting = channelPlaylistRepo.getPlaylistChannelOrderByChannel(channel);

        playlistForSorting.stream().sorted(orderNumber).collect(Collectors.toList());

        return playlistForSorting;
    }

    @Override
    public void removePlaylistFromChannel(String channelId, String playlistId) {

        Playlist playlist = playlistService.getPlaylist(playlistId);

        Channel channel = channelService.getChannel(channelId);

        Optional<PlaylistChannelOrder> playlistChannelOrder = channelPlaylistRepo.getPlaylistChannelOrderByChannelAndPlaylist(channel, playlist);

        if(playlistChannelOrder.isEmpty()) throw new ResourceMissingException();

        channelPlaylistRepo.delete(playlistChannelOrder.get());
    }

    @Override
    public List<PlaylistChannelOrder> playlistIndex(String channelId, String playlistId, Integer newIndex) {
        Channel channel = channelService.getChannel(channelId);

        Playlist playlist = playlistService.getPlaylist(playlistId);

        Optional<PlaylistChannelOrder> foundPlaylist = channelPlaylistRepo.getPlaylistChannelOrderByChannelAndPlaylist(channel, playlist);

        if(foundPlaylist.isEmpty()){
            throw new PlaylistNotInChannelException();
        }

        List<PlaylistChannelOrder> channelOrderList = channelPlaylistRepo.getPlaylistChannelOrderByChannel(channel);

        Integer indexOfPlaylist = channelOrderList.indexOf(playlist);

        PlaylistChannelOrder playlistInChannel = channelOrderList.get(indexOfPlaylist);

        if (newIndex > channelOrderList.size()) {
            playlistInChannel.setOrderNumber(channelOrderList.size());
            channelPlaylistRepo.save(playlistInChannel);
        }

        if(newIndex > indexOfPlaylist) {

            for (PlaylistChannelOrder p : channelOrderList) {

                if (p.getOrderNumber() <= newIndex && p.getOrderNumber() >= indexOfPlaylist) {
                    p.setOrderNumber(p.getOrderNumber() - 1);

                }
                channelPlaylistRepo.save(p);
            }
            playlistInChannel.setOrderNumber(newIndex);
            channelPlaylistRepo.save(playlistInChannel);

        }
        if(newIndex < indexOfPlaylist) {
            for (PlaylistChannelOrder p : channelOrderList) {
                if (p.getOrderNumber() >= newIndex && p.getOrderNumber() <= indexOfPlaylist) {
                    p.setOrderNumber(p.getOrderNumber() + 1);
                }

                channelPlaylistRepo.save(p);
            }
            playlistInChannel.setOrderNumber(newIndex);
            channelPlaylistRepo.save(playlistInChannel);
        }




        Comparator<PlaylistChannelOrder> orderNumber = Comparator.comparing(PlaylistChannelOrder::getOrderNumber);

        channelOrderList.stream().sorted(orderNumber).collect(Collectors.toList());



        return channelOrderList;
    }

    @Override
    public PlaylistChannelOrder addPlaylistToChannel(String channelId, String playlistId) {
        Playlist playlist = playlistService.getPlaylist(playlistId);

        Channel channel = channelService.getChannel(channelId);

        PlaylistChannelOrder playlistChannelOrder = new PlaylistChannelOrder();

        playlistChannelOrder.setChannel(channel);

        playlistChannelOrder.setPlaylist(playlist);

        List<PlaylistChannelOrder> allPlaylistsInChannel = channelPlaylistRepo.getPlaylistChannelOrderByChannel(channel);

        if(allPlaylistsInChannel.isEmpty()){
            playlistChannelOrder.setOrderNumber(1);
        }
        else{
            playlistChannelOrder.setOrderNumber(allPlaylistsInChannel.get(allPlaylistsInChannel.size() - 1).getOrderNumber() + 1);
        }
        channelPlaylistRepo.save(playlistChannelOrder);
        return playlistChannelOrder;
    }
}
