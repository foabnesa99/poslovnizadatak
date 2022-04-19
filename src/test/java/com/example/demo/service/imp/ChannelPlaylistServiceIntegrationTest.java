package com.example.demo.service.imp;

import com.example.demo.model.*;
import com.example.demo.repo.*;
import com.example.demo.service.ChannelPlaylistService;
import com.example.demo.service.ChannelService;
import com.example.demo.service.PlaylistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ChannelPlaylistServiceIntegrationTest {

    @Autowired
    PlaylistRepo playlistRepo;

    @Autowired
    ChannelPlaylistService channelPlaylistService;
    @Autowired
    ChannelRepo channelRepo;

    @Autowired
    ChannelService channelService;

    @Autowired
    PlaylistService playlistService;

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    ChannelPlaylistRepo channelPlaylistRepo;

    @Autowired
    UserRepo userRepo;

    @BeforeEach
    void setUp() {

        Playlist playlist1 = playlistRepo.save(new Playlist("Prva test plejlista", new ArrayList<>(List.of(categoryRepo.save(new Category("Horor"))))));
        Playlist playlist2 = playlistRepo.save(new Playlist("Druga test plejlista", new ArrayList<>(List.of(categoryRepo.save(new Category("Akcija"))))));
        Playlist playlist3 = playlistRepo.save(new Playlist("Treca test plejlista", new ArrayList<>(List.of(categoryRepo.save(new Category("Triler"))))));
        Playlist playlist4 = playlistRepo.save(new Playlist("Cetvrta test plejlista", new ArrayList<>(List.of(categoryRepo.save(new Category("Komedija"))))));
        Playlist playlist5 = playlistRepo.save(new Playlist("Peta test plejlista", new ArrayList<>(List.of(categoryRepo.save(new Category("Dokumentarni"))))));

        Channel channel1 = channelRepo.save(new Channel("Prvi kanal test", userRepo.save(new User("Pera peric"))));
        Channel channel2 = channelRepo.save(new Channel("Drugi kanal test", userRepo.save(new User("Dragance"))));

        PlaylistChannel pc1 = channelPlaylistService.addPlaylistToChannel("1", "1");
        PlaylistChannel pc2 = channelPlaylistService.addPlaylistToChannel("1", "2");
        PlaylistChannel pc3 = channelPlaylistService.addPlaylistToChannel("1", "3");
        PlaylistChannel pc4 = channelPlaylistService.addPlaylistToChannel("1", "4");
        PlaylistChannel pc5 = channelPlaylistService.addPlaylistToChannel("1", "5");
    }

    @Test
    void removePlaylistFromChannel() {
        Optional<PlaylistChannel> deletedItem = channelPlaylistRepo.getPlaylistChannelByChannelAndPlaylist(channelRepo.getById("1"), playlistRepo.getById("2"));
        channelPlaylistService.removePlaylistFromChannel("1", "2");
        List<PlaylistChannel> playlistChannelList = channelPlaylistRepo.getPlaylistChannelsByChannel(channelRepo.getById("1"));

        assertThat(playlistChannelList).doesNotContain(deletedItem.get());
        assertThat(playlistChannelList).size().isEqualTo(4);
        assertThat(playlistChannelList.get(1).getOrderNumber()).isEqualTo(2);
        assertThat(playlistChannelList.get(2).getOrderNumber()).isEqualTo(3);

    }

    @Test
    void playlistIndex() {

        List<PlaylistChannel> playlistChannelList = channelPlaylistService.playlistIndex("1", "1", 3);
        System.out.println("\n \n" + playlistChannelList + "LISTA ELEMENATA \n \n");
        PlaylistChannel pco = channelPlaylistRepo.getPlaylistChannelByChannelAndPlaylist(channelRepo.getById("1"), playlistRepo.getById("1")).get();
        System.out.println("\n 'n" + pco + "JEDAN ELEMENT \n \n");
        int indexOfElement = playlistChannelList.indexOf(pco);
        assertThat(playlistChannelList.get(indexOfElement).getOrderNumber()).isEqualTo(3);
        assertThat(playlistChannelList.get(indexOfElement + 1).getOrderNumber()).isEqualTo(1);
        assertThat(playlistChannelList.get(indexOfElement + 2).getOrderNumber()).isEqualTo(2);
        assertThat(playlistChannelList.get(indexOfElement + 3).getOrderNumber()).isEqualTo(4);
        assertThat(playlistChannelList.get(indexOfElement + 4).getOrderNumber()).isEqualTo(5);

    }

}