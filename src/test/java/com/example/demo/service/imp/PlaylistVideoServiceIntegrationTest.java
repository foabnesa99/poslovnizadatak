package com.example.demo.service.imp;

import com.example.demo.model.Category;
import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import com.example.demo.model.VideoPlaylist;
import com.example.demo.repo.CategoryRepo;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.repo.VideoPlaylistRepo;
import com.example.demo.repo.VideoRepo;
import com.example.demo.service.PlaylistVideoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PlaylistVideoServiceIntegrationTest {

    @Autowired
    private PlaylistVideoService playlistVideoService;

    @Autowired
   private PlaylistRepo playlistRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
   private VideoPlaylistRepo videoPlaylistRepo;

    @Autowired
    private VideoRepo videoRepo;



   @BeforeEach

   public void populateRepo(){

       Category category1 = categoryRepo.save(new Category("Thriller"));
       Category category2 = categoryRepo.save(new Category("Comedy"));
       Video video1 = new Video("Z Video macka", "url",new HashSet<>(List.of(category1)));
       video1.setId("1");
       Video video2 = new Video("D Video pas","url" ,new HashSet<>(List.of(category2)));
       video2.setId("2");
       Video video5 = new Video("T Sjajan video macke kako pada u vodu","url" ,new HashSet<>(List.of(categoryRepo.save(new Category("Horror")))));
       video5.setId("3");
       Video video6 = new Video("B Video kako se penje lik","url" ,new HashSet<>(List.of(categoryRepo.save(new Category("Action")))));
        video6.setId("4");

       videoRepo.save(video1);
       videoRepo.save(video2);
       videoRepo.save(video5);
       videoRepo.save(video6);

        Playlist playlist1 = new Playlist("Playlist 1" , new HashSet<>(List.of(category1)));
        playlist1.setId("1");

        playlistRepo.save(playlist1);

        VideoPlaylist vpo1 = new VideoPlaylist();
        vpo1.setPlaylist(playlist1);
        vpo1.setVideo(video1);
        vpo1.setOrderNumber(1);
        vpo1.setId("1");

        VideoPlaylist vpo2 = new VideoPlaylist();
        vpo2.setPlaylist(playlist1);
        vpo2.setVideo(video2);
        vpo2.setOrderNumber(2);
        vpo2.setId("2");

       VideoPlaylist vpo3 = new VideoPlaylist();
       vpo3.setPlaylist(playlist1);
       vpo3.setVideo(video5);
       vpo3.setOrderNumber(3);
       vpo3.setId("3");

       VideoPlaylist vpo4 = new VideoPlaylist();
       vpo4.setPlaylist(playlist1);
       vpo4.setVideo(video6);
       vpo4.setOrderNumber(4);
       vpo4.setId("4");

       videoPlaylistRepo.save(vpo1);
        videoPlaylistRepo.save(vpo2);
        videoPlaylistRepo.save(vpo3);
        videoPlaylistRepo.save(vpo4);

   }

   @Test
    public void videoGetsRemovedFromPlaylistTest(){
       Optional<VideoPlaylist> deletedItem = videoPlaylistRepo.getVideoPlaylistByPlaylistAndVideo(playlistRepo.getById("1"), videoRepo.getById("2"));
       playlistVideoService.removeVideoFromPlaylist("1", "2");
       List<VideoPlaylist> videoPlaylists = videoPlaylistRepo.getVideoPlaylistsByPlaylist(playlistRepo.getById("1"));

       assertThat(videoPlaylists).doesNotContain(deletedItem.get());
       assertThat(videoPlaylists).size().isEqualTo(3);
       assertThat(videoPlaylists.get(1).getOrderNumber()).isEqualTo(2);
       assertThat(videoPlaylists.get(2).getOrderNumber()).isEqualTo(3);

   }

   @Test

    public void changeVideoIndexTest(){
       List<VideoPlaylist> playlist = playlistVideoService.videoIndex("1", "1", 3);
       VideoPlaylist vpo = videoPlaylistRepo.getVideoPlaylistByPlaylistAndVideo(playlistRepo.getById("1"), videoRepo.getById("1")).get();
       int indexOfElement = playlist.indexOf(vpo);

       assertThat(playlist.get(indexOfElement).getOrderNumber()).isEqualTo(3);
       assertThat(playlist.get(indexOfElement + 1).getOrderNumber()).isEqualTo(1);
       assertThat(playlist.get(indexOfElement + 2).getOrderNumber()).isEqualTo(2);


   }

}
