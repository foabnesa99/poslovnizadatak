package com.example.demo.unittesting;

import com.example.demo.model.Category;
import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import com.example.demo.model.VideoPlaylistOrder;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.repo.VideoPlaylistOrderRepo;
import com.example.demo.repo.VideoRepo;
import com.example.demo.service.PlaylistService;
import com.example.demo.service.PlaylistVideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("Test")
public class PlaylistVideoServiceUnitTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    PlaylistVideoService playlistVideoService;

    @MockBean
    VideoPlaylistOrderRepo videoPlaylistOrderRepo;

    @MockBean
    CategoryRepo categoryRepo;

    @MockBean
    PlaylistService playlistService;

    @MockBean
    VideoRepo videoRepo;

    @Autowired
    PlaylistRepo playlistRepo;

    @BeforeEach
    public void dataInit(){

        Playlist playlist1 = new Playlist("Prva test plejlista" , new ArrayList<>(Arrays.asList(new Category("Horror"))));
        when(playlistService.getPlaylist("1")).thenReturn(playlist1);
        Video video1 = new Video("Prvi test video", new ArrayList<>(Arrays.asList(new Category("Horror"))));
        video1.setId("1");
        when(videoRepo.findById("1")).thenReturn(Optional.of(video1));
        Video video2 = new Video("Drugi test video", new ArrayList<>(Arrays.asList(new Category("Thriller"))));
        video2.setId("2");
        Video video3 = new Video("Treci test video", new ArrayList<>(Arrays.asList(new Category("Action"))));
        video3.setId("3");
        Video video4 = new Video("Cetvrti test video", new ArrayList<>(Arrays.asList(new Category("Comedy"))));
        video4.setId("4");
        Video video5 = new Video("Peti test video", new ArrayList<>(Arrays.asList(new Category("Documentary"))));
        video5.setId("5");

        when(videoRepo.findById("3")).thenReturn(Optional.of(video3));
        when(videoRepo.findById("2")).thenReturn(Optional.of(video2));
        when(videoRepo.findById("4")).thenReturn(Optional.of(video4));
        when(videoRepo.findById("5")).thenReturn(Optional.empty());
        VideoPlaylistOrder vpl1 = new VideoPlaylistOrder("1",video1 , playlist1 , 2);
        Optional<VideoPlaylistOrder> vpl2 = Optional.empty();
        VideoPlaylistOrder vpl3 = new VideoPlaylistOrder("3",video3 , playlist1 , 3);
        VideoPlaylistOrder vpl4 = new VideoPlaylistOrder("4",video4 , playlist1 , 1);
        VideoPlaylistOrder vpl5 = new VideoPlaylistOrder("5",video5 , playlist1 , 4);

        when(videoPlaylistOrderRepo.getVideoPlaylistOrderByPlaylistAndVideo(playlist1, video1)).thenReturn(Optional.of(vpl1));
        when(videoPlaylistOrderRepo.getVideoPlaylistOrderByPlaylistAndVideo(playlist1, video2)).thenReturn(vpl2);
        when(videoPlaylistOrderRepo.getVideoPlaylistOrderByPlaylistAndVideo(playlist1, video3)).thenReturn(Optional.of(vpl3));
        when(videoPlaylistOrderRepo.getVideoPlaylistOrderByPlaylistAndVideo(playlist1, video4)).thenReturn(Optional.of(vpl4));

        List<VideoPlaylistOrder> playlistVideoServiceList = new ArrayList<>(Arrays.asList(vpl1, vpl3 , vpl4));

        System.out.println(playlistVideoServiceList + "\n OVO JE NESORTIRANA PLEJLISTA \n \n");

        when(videoPlaylistOrderRepo.getVideoPlaylistOrdersByPlaylist(playlist1)).thenReturn(playlistVideoServiceList);

    }

    @Test
    public void sortPlaylistVideo(){
        List<VideoPlaylistOrder> playlists = playlistVideoService.videoSort("1");
        System.out.println(playlists + " \n OVO JE SORTIRANA PLEJLISTA \n \n");
        assertThat(playlists.get(0).getOrderNumber()).isEqualTo(1);
        assertThat(playlists.get(1).getOrderNumber()).isEqualTo(2);
        assertThat(playlists.get(2).getOrderNumber()).isEqualTo(3);

    }

}
