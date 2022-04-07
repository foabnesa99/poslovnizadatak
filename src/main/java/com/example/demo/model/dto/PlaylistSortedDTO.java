package com.example.demo.model.dto;

import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import lombok.Data;

@Data
public class PlaylistSortedDTO {

    private String id;

    private Video video;

    private Playlist playlist;

    private Integer orderNumber;

}
