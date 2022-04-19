package com.example.demo.controller;

import com.example.demo.model.Playlist;
import com.example.demo.model.PlaylistChannel;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.ChannelRepo;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.service.*;
import com.example.demo.util.exceptions.ChannelMissingException;
import com.example.demo.util.exceptions.PlaylistMissingException;
import com.example.demo.util.exceptions.ResourceMissingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ChannelController.class)
@AutoConfigureMockMvc
class ChannelControllerTest {

    @MockBean
    ChannelPlaylistService channelPlaylistService;

    @MockBean
    ChannelService channelService;

    @MockBean
    PlaylistVideoService playlistVideoService;

    @MockBean
    VideoService videoService;

    @MockBean
    PlaylistService playlistService;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        when(channelPlaylistService.playlistSort("3")).thenReturn(new ArrayList<>(List.of(new PlaylistChannel())));
        when(channelPlaylistService.playlistSort("1")).thenThrow(new ChannelMissingException());
        when(channelPlaylistService.removePlaylistFromChannel("1","20")).thenThrow(new ResourceMissingException());
    }

    @Test
    void channelSortTest_ChannelNotFound_ShouldThrowException () throws Exception {
        mockMvc.perform(put("/api/channels/1/sort")).andExpect(status().isNotFound());
    }

    @Test
    void channelSortTest_ChannelExists_ShouldReturnOK() throws Exception {
        mockMvc.perform(put("/api/channels/3/sort")).andExpect(status().isOk());
    }

    @Test
    void playlistRemoveTest_ShouldThrowException() throws Exception {
        mockMvc.perform(delete("/api/channels/1/playlists/delete/20")).andExpect(status().isNotFound());
    }

    @Test
    void playlistRemoveTest_ShouldReturnOk() throws Exception{
        mockMvc.perform(delete("/api/channels/1/playlists/delete/5")).andExpect(status().isOk());
    }
}