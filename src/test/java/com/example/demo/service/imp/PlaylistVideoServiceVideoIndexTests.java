package com.example.demo.service.imp;


import com.example.demo.model.Category;
import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import com.example.demo.model.VideoPlaylist;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.repo.VideoPlaylistRepo;
import com.example.demo.repo.VideoRepo;
import com.example.demo.service.PlaylistService;
import com.example.demo.service.PlaylistVideoService;
import com.example.demo.util.exceptions.ResourceMissingException;
import com.example.demo.util.exceptions.VideoMissingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PlaylistVideoServiceVideoIndexTests {

    @MockBean
    PlaylistRepo playlistRepo;

    @MockBean
    PlaylistService playlistService;

    @MockBean
    VideoRepo videoRepo;

    @MockBean
    VideoPlaylistRepo videoPlaylistRepo;

    @Autowired
    PlaylistVideoService playlistVideoService;

    @BeforeEach
    public void dataInit(){

       Playlist playlist1 = new Playlist("Prva test plejlista" , new HashSet<>(List.of(new Category("Horror"))));
        when(playlistService.getPlaylist(eq("1"))).thenReturn(playlist1);
        Video video1 = new Video("Prvi test video","url" ,new HashSet<>(List.of(new Category("Horror"))));
        video1.setId("1");
        when(videoRepo.findById(eq("1"))).thenReturn(Optional.of(video1));
        Video video2 = new Video("Drugi test video", "url",new HashSet<>(List.of(new Category("Thriller"))));
        video2.setId("2");
        Video video3 = new Video("Treci test video","url" ,new HashSet<>(List.of(new Category("Action"))));
        video3.setId("3");
        Video video4 = new Video("Cetvrti test video","url" ,new HashSet<>(List.of(new Category("Comedy"))));
        video4.setId("4");
        Video video5 = new Video("Peti test video","url" ,new HashSet<>(List.of(new Category("Documentary"))));
        video5.setId("5");

        when(videoRepo.findById(eq("3"))).thenReturn(Optional.of(video3));
        when(videoRepo.findById(eq("2"))).thenReturn(Optional.of(video2));
        when(videoRepo.findById(eq("4"))).thenReturn(Optional.of(video4));
        when(videoRepo.findById(eq("5"))).thenReturn(Optional.empty());
        VideoPlaylist vpl1 = new VideoPlaylist("1",video1 , playlist1 , 1);
        Optional<VideoPlaylist> vpl2 = Optional.empty();
        VideoPlaylist vpl3 = new VideoPlaylist("3",video3 , playlist1 , 2);
        VideoPlaylist vpl4 = new VideoPlaylist("4",video4 , playlist1 , 3);
        VideoPlaylist vpl5 = new VideoPlaylist("5",video5 , playlist1 , 4);

        when(videoPlaylistRepo.getVideoPlaylistByPlaylistAndVideo(eq(playlist1), eq(video1))).thenReturn(Optional.of(vpl1));
        when(videoPlaylistRepo.getVideoPlaylistByPlaylistAndVideo(eq(playlist1), eq(video2))).thenReturn(vpl2);
        when(videoPlaylistRepo.getVideoPlaylistByPlaylistAndVideo(eq(playlist1), eq(video3))).thenReturn(Optional.of(vpl3));
        when(videoPlaylistRepo.getVideoPlaylistByPlaylistAndVideo(eq(playlist1), eq(video4))).thenReturn(Optional.of(vpl4));

        List<VideoPlaylist> playlistVideoServiceList = new ArrayList<>(Arrays.asList(vpl1, vpl3 , vpl4));

        when(videoPlaylistRepo.getVideoPlaylistsByPlaylist(eq(playlist1))).thenReturn(playlistVideoServiceList);

    }

    @Test
    public void videoChangeIndex_VideoNotExist_ShouldThrowException(){
        Assertions.assertThrows(VideoMissingException.class, () -> playlistVideoService.videoIndex("1","5",1));
    }

    @Test
    public void videoChangeIndex_VideoNotInPlaylist_ShouldThrowException(){
        Assertions.assertThrows(ResourceMissingException.class, () -> playlistVideoService.videoIndex("1","2",1)
        );
    }

    @Test
    public void videoChangeIndex_NewIndexOutOfBounds_ShouldSetIndex(){
        List<VideoPlaylist> playlists = playlistVideoService.videoIndex("1", "1" , 20);
        Assertions.assertEquals(3, playlists.get(0).getOrderNumber());
        Assertions.assertEquals(2, playlists.get(2).getOrderNumber());
        Assertions.assertEquals(1, playlists.get(1).getOrderNumber());
    }

    @Test
    public void videoChangeIndex_NewIndexLargerThanVideoIndex_ShouldSetIndex(){
        List<VideoPlaylist> playlists = playlistVideoService.videoIndex("1", "1" , 2);
        Assertions.assertEquals(2, playlists.get(0).getOrderNumber());
        Assertions.assertEquals(3, playlists.get(2).getOrderNumber());
        Assertions.assertEquals(1, playlists.get(1).getOrderNumber());
    }

    @Test
    public void videoChangeIndex_NewIndexSmallerThanVideoIndex_ShouldSetIndex(){
        List<VideoPlaylist> playlists = playlistVideoService.videoIndex("1", "4" , 1);
        System.out.println(playlists);
        Assertions.assertEquals(2, playlists.get(0).getOrderNumber());
        Assertions.assertEquals(1, playlists.get(2).getOrderNumber());
        Assertions.assertEquals(3, playlists.get(1).getOrderNumber());

    }

}
