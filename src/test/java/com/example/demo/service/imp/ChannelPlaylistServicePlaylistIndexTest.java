package com.example.demo.service.imp;

import com.example.demo.model.*;
import com.example.demo.repo.ChannelPlaylistRepo;
import com.example.demo.service.ChannelPlaylistService;
import com.example.demo.service.ChannelService;
import com.example.demo.service.PlaylistService;
import com.example.demo.util.exceptions.PlaylistMissingException;
import com.example.demo.util.exceptions.PlaylistNotInChannelException;
import com.example.demo.util.exceptions.ResourceMissingException;
import com.example.demo.util.exceptions.VideoMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringBootTest
public class ChannelPlaylistServicePlaylistIndexTest {

    @MockBean
    ChannelService channelService;

    @MockBean
    PlaylistService playlistService;

    @MockBean
    ChannelPlaylistRepo channelPlaylistRepo;
    @Autowired
    ChannelPlaylistService channelPlaylistService;

    @BeforeEach
    public void setUp(){


        Playlist playlist1 = new Playlist("Prva test plejlista", new ArrayList<>(List.of(new Category("Triler"))));
        playlist1.setId("1");
        when(playlistService.getPlaylist("1")).
                thenReturn(playlist1);
        Playlist playlist2 = new Playlist("Druga test plejlista", new ArrayList<>(List.of(new Category("Akcija"))));
        playlist2.setId("2");
        when(playlistService.getPlaylist("2")).
                thenReturn(playlist2);
        Playlist playlist3 = new Playlist("Treca test plejlista", new ArrayList<>(List.of(new Category("Horor"))));
        playlist3.setId("3");
        when(playlistService.getPlaylist("3")).
                thenReturn(playlist3);
        Playlist playlist4 = new Playlist("Cetvrta test plejlista", new ArrayList<>(List.of(new Category("Komedija"))));
        playlist4.setId("4");
        when(playlistService.getPlaylist("4")).
                thenReturn(playlist4);
        Playlist playlist5 = new Playlist("Peta test plejlista", new ArrayList<>(List.of(new Category("Dokumentarni"))));
        playlist5.setId("5");
        when(playlistService.getPlaylist("5")).
                thenReturn(playlist5);

        Channel channel1 = new Channel("Perin kanal", new User("Petar petrovic"));
        channel1.setId("1");
        when(channelService.getChannel("1"))
                .thenReturn(channel1);
        Channel channel2 = new Channel("Draganov kanal", new User("Dragan Mitrovic"));
        channel2.setId("2");
        when(channelService.getChannel("2"))
                .thenReturn(channel2);

        PlaylistChannel cpl1 = new PlaylistChannel();
        cpl1.setChannel(channel1);
        cpl1.setPlaylist(playlist1);
        cpl1.setId("1");
        cpl1.setOrderNumber(1);

        PlaylistChannel cpl2 = new PlaylistChannel();
        cpl2.setChannel(channel1);
        cpl2.setPlaylist(playlist2);
        cpl2.setId("2");
        cpl2.setOrderNumber(2);

        PlaylistChannel cpl3 = new PlaylistChannel();
        cpl3.setChannel(channel1);
        cpl3.setPlaylist(playlist3);
        cpl3.setId("3");
        cpl3.setOrderNumber(3);

        PlaylistChannel cpl4 = new PlaylistChannel();
        cpl4.setChannel(channel1);
        cpl4.setPlaylist(playlist4);
        cpl4.setId("4");
        cpl4.setOrderNumber(4);

        when(channelPlaylistRepo.getPlaylistChannelByChannelAndPlaylist(channel1, playlist1)).thenReturn(Optional.of(cpl1));
        when(channelPlaylistRepo.getPlaylistChannelByChannelAndPlaylist(channel1, playlist2)).thenReturn(Optional.of(cpl2));
        when(channelPlaylistRepo.getPlaylistChannelByChannelAndPlaylist(channel1, playlist3)).thenReturn(Optional.of(cpl3));
        when(channelPlaylistRepo.getPlaylistChannelByChannelAndPlaylist(channel1, playlist4)).thenReturn(Optional.of(cpl4));

        when(channelPlaylistRepo.getPlaylistChannelsByChannel(channel1)).thenReturn(new ArrayList<>(Arrays.asList(cpl1, cpl2, cpl3, cpl4)));

    }

    @Test
    public void videoChangeIndex_VideoNotInPlaylist_ShouldThrowException(){
        Assertions.assertThrows(PlaylistNotInChannelException.class, () -> channelPlaylistService.playlistIndex("1","12",1)
        );
    }

    @Test
    public void videoChangeIndex_NewIndexOutOfBounds_ShouldSetIndex(){
        List<PlaylistChannel> playlistChannelList = channelPlaylistService.playlistIndex("1", "1" , 20);
        Assertions.assertEquals(4, playlistChannelList.get(0).getOrderNumber());
        Assertions.assertEquals(2, playlistChannelList.get(2).getOrderNumber());
        Assertions.assertEquals(1, playlistChannelList.get(1).getOrderNumber());
    }

    @Test
    public void videoChangeIndex_NewIndexLargerThanVideoIndex_ShouldSetIndex(){
        List<PlaylistChannel> playlistChannelList = channelPlaylistService.playlistIndex("1", "1" , 2);
        Assertions.assertEquals(2, playlistChannelList.get(0).getOrderNumber());
        Assertions.assertEquals(3, playlistChannelList.get(2).getOrderNumber());
        Assertions.assertEquals(1, playlistChannelList.get(1).getOrderNumber());
    }

    @Test
    public void videoChangeIndex_NewIndexSmallerThanVideoIndex_ShouldSetIndex(){
        List<PlaylistChannel> playlistChannelList = channelPlaylistService.playlistIndex("1", "4" , 1);
        System.out.println(playlistChannelList);
        Assertions.assertEquals(2, playlistChannelList.get(0).getOrderNumber());
        Assertions.assertEquals(4, playlistChannelList.get(2).getOrderNumber());
        Assertions.assertEquals(3, playlistChannelList.get(1).getOrderNumber());
        Assertions.assertEquals(1, playlistChannelList.get(3).getOrderNumber());

    }

}
