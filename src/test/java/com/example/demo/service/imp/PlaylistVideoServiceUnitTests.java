package com.example.demo.service.imp;

import com.example.demo.model.Category;
import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import com.example.demo.model.VideoPlaylist;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.repo.VideoPlaylistRepo;
import com.example.demo.repo.VideoRepo;
import com.example.demo.service.PlaylistService;
import com.example.demo.service.PlaylistVideoService;
import com.example.demo.service.VideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.list;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("Test")
public class PlaylistVideoServiceUnitTests {

    @Autowired
    PlaylistVideoService playlistVideoService;

    @MockBean
    VideoPlaylistRepo videoPlaylistRepo;

    @MockBean
    CategoryRepo categoryRepo;

    @MockBean
    PlaylistService playlistService;

    @MockBean
    VideoService videoService;

    @MockBean
    VideoRepo videoRepo;

    @Autowired
    PlaylistRepo playlistRepo;

    @BeforeEach
    public void dataInit(){

        Playlist playlist1 = new Playlist("Prva test plejlista" , new ArrayList<>(List.of(new Category("Horror"))));
        when(playlistService.getPlaylist("1")).thenReturn(playlist1);
        Video video1 = new Video("Prvi test video", new ArrayList<>(List.of(new Category("Horror"))));
        video1.setId("1");
        when(videoRepo.findById("1")).thenReturn(Optional.of(video1));
        Video video2 = new Video("Drugi test video", new ArrayList<>(List.of(new Category("Thriller"))));
        video2.setId("2");
        Video video3 = new Video("Treci test video", new ArrayList<>(List.of(new Category("Action"))));
        video3.setId("3");
        Video video4 = new Video("Cetvrti test video", new ArrayList<>(List.of(new Category("Comedy"))));
        video4.setId("4");
        Video video5 = new Video("Peti test video", new ArrayList<>(List.of(new Category("Documentary"))));
        video5.setId("5");

        when(videoService.getVideo("1")).thenReturn(video1);
        when(videoService.getVideo("2")).thenReturn(video2);
        when(videoService.getVideo("3")).thenReturn(video3);
        when(videoService.getVideo("4")).thenReturn(video4);
        when(videoService.getVideo("5")).thenReturn(video5);

        when(videoRepo.findById("3")).thenReturn(Optional.of(video3));
        when(videoRepo.findById("2")).thenReturn(Optional.of(video2));
        when(videoRepo.findById("4")).thenReturn(Optional.of(video4));
        when(videoRepo.findById("5")).thenReturn(Optional.empty());

        VideoPlaylist vpl1 = new VideoPlaylist("1",video1 , playlist1 , 2);
        Optional<VideoPlaylist> vpl2 = Optional.empty();
        VideoPlaylist vpl3 = new VideoPlaylist("3",video3 , playlist1 , 3);
        VideoPlaylist vpl4 = new VideoPlaylist("4",video4 , playlist1 , 1);
        VideoPlaylist vpl5 = new VideoPlaylist("5",video5 , playlist1 , 4);

        when(videoPlaylistRepo.getVideoPlaylistByPlaylistAndVideo(playlist1, video1)).thenReturn(Optional.of(vpl1));
        when(videoPlaylistRepo.getVideoPlaylistByPlaylistAndVideo(playlist1, video2)).thenReturn(vpl2);
        when(videoPlaylistRepo.getVideoPlaylistByPlaylistAndVideo(playlist1, video3)).thenReturn(Optional.of(vpl3));
        when(videoPlaylistRepo.getVideoPlaylistByPlaylistAndVideo(playlist1, video4)).thenReturn(Optional.of(vpl4));
        when(videoPlaylistRepo.getVideoPlaylistByPlaylistAndVideo(playlist1, video5)).thenReturn(Optional.of(vpl5));

        List<VideoPlaylist> playlistVideoServiceList = new ArrayList<>(Arrays.asList(vpl1, vpl3 , vpl4, vpl5));
        when(videoPlaylistRepo.getVideoPlaylistsByPlaylist(playlist1)).thenReturn(playlistVideoServiceList);

    }

    @Test
    public void sortPlaylistVideo(){
        List<VideoPlaylist> playlists = playlistVideoService.videoSort("1");
        assertThat(playlists.get(0).getOrderNumber()).isEqualTo(1);
        assertThat(playlists.get(1).getOrderNumber()).isEqualTo(2);
        assertThat(playlists.get(2).getOrderNumber()).isEqualTo(3);

    }

    @Test
    public void videoRemovePlaylist(){
        Playlist playlist1 = playlistService.getPlaylist("1");
        Video video1 = videoService.getVideo("1");
        List<VideoPlaylist> listAfterRemove = playlistVideoService.removeVideoFromPlaylist("1", "1");
        System.out.println("New list \n \n \n " + listAfterRemove);
        assertThat(listAfterRemove).doesNotContain(videoPlaylistRepo.getVideoPlaylistByPlaylistAndVideo(playlist1, video1).get());
    }


}
