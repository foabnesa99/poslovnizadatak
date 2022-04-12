package com.example.demo;


import com.example.demo.controller.PlaylistController;
import com.example.demo.service.PlaylistVideoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@ExtendWith(SpringExtension.class)
@WebMvcTest(PlaylistController.class)
public class PlaylistControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PlaylistVideoService playlistVideoService;

    @Test
    public void videoDeleteTest() throws Exception{


    }
}
