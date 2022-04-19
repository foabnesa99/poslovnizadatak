package com.example.demo.controller;

import com.example.demo.repo.VideoPlaylistRepo;
import com.example.demo.repo.VideoRepo;
import com.example.demo.service.PlaylistService;
import com.example.demo.service.PlaylistVideoService;
import com.example.demo.service.VideoService;
import com.example.demo.util.exceptions.PlaylistMissingException;
import com.example.demo.util.exceptions.ResourceMissingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
    VideoPlaylistRepo videoPlaylistRepo;

    @BeforeEach
    public void dataInit(){

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
