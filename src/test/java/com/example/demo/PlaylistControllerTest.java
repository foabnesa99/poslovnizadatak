package com.example.demo;


import com.example.demo.controller.PlaylistController;
import com.example.demo.model.Category;
import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import com.example.demo.model.VideoPlaylistOrder;
import com.example.demo.repo.VideoPlaylistOrderRepo;
import com.example.demo.repo.VideoRepo;
import com.example.demo.service.PlaylistService;
import com.example.demo.service.PlaylistVideoService;
import com.example.demo.service.VideoService;
import com.example.demo.util.exceptions.PlaylistMissingException;
import com.example.demo.util.exceptions.ResourceMissingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.web.servlet.function.RequestPredicates.contentType;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PlaylistController.class)
@AutoConfigureMockMvc
public class PlaylistControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    VideoService videoService;

    @MockBean
    PlaylistVideoService playlistVideoService;

    @MockBean
    PlaylistService playlistService;

    @MockBean
    VideoRepo videoRepo;

    @MockBean
    VideoPlaylistOrderRepo videoPlaylistOrderRepo;

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
        VideoPlaylistOrder vpl1 = new VideoPlaylistOrder("1",video1 , playlist1 , 1);
        Optional<VideoPlaylistOrder> vpl2 = Optional.empty();
        VideoPlaylistOrder vpl3 = new VideoPlaylistOrder("3",video3 , playlist1 , 2);
        VideoPlaylistOrder vpl4 = new VideoPlaylistOrder("4",video4 , playlist1 , 3);
        VideoPlaylistOrder vpl5 = new VideoPlaylistOrder("5",video5 , playlist1 , 4);

        when(videoPlaylistOrderRepo.getVideoPlaylistOrderByPlaylistAndVideo(playlist1, video1)).thenReturn(Optional.of(vpl1));
        when(videoPlaylistOrderRepo.getVideoPlaylistOrderByPlaylistAndVideo(playlist1, video2)).thenReturn(vpl2);
        when(videoPlaylistOrderRepo.getVideoPlaylistOrderByPlaylistAndVideo(playlist1, video3)).thenReturn(Optional.of(vpl3));
        when(videoPlaylistOrderRepo.getVideoPlaylistOrderByPlaylistAndVideo(playlist1, video4)).thenReturn(Optional.of(vpl4));

        List<VideoPlaylistOrder> playlistVideoServiceList = new ArrayList<>(Arrays.asList(vpl1, vpl3 , vpl4));

        when(videoPlaylistOrderRepo.getVideoPlaylistOrdersByPlaylist(playlist1)).thenReturn(playlistVideoServiceList);
        when(playlistVideoService.videoSort("1")).thenThrow(new PlaylistMissingException());
        when(playlistVideoService.removeVideoFromPlaylist("2","12")).thenThrow(new ResourceMissingException());
    }

    @Test
    public void playlistSortTest_ShouldThrowPlaylistMissingException() throws Exception {
        mockMvc.perform(put("/api/playlists/1/sort")).andExpect(status().isNotFound());
    }

    @Test
    public void playlistSortTest_ShouldReturnOK() throws Exception{
        mockMvc.perform(put("/api/playlists/2/sort")).andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void videoRemoveTest_ShouldThrowException() throws Exception{
        mockMvc.perform(delete("/api/playlists/2/videos/delete/12")).andExpect(status().isNotFound());
    }

    @Test
    public void videoRemoveTest_ShouldReturnOK() throws Exception{
        mockMvc.perform(delete("/api/playlists/1/videos/delete/1")).andExpect(status().isOk());
    }

}