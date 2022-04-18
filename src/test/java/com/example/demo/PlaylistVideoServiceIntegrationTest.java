package com.example.demo;

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
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.slf4j.*;


import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PlaylistVideoServiceIntegrationTest {

    private static Logger logger = LoggerFactory.getLogger(PlaylistVideoServiceIntegrationTest.class);

    @Autowired
    private PlaylistVideoService playlistVideoService;

    @Autowired
   private PlaylistRepo playlistRepo;

    @Autowired
    private CategoryRepo categoryRepo;

    @Autowired
   private VideoPlaylistOrderRepo videoPlaylistOrderRepo;

    @Autowired
    private VideoRepo videoRepo;

   private PlaylistService playlistService = Mockito.mock(PlaylistService.class);


   @BeforeEach

   public void populateRepo(){

       Category category1 = categoryRepo.save(new Category("Thriller"));
       Category category2 = categoryRepo.save(new Category("Comedy"));
       Video video1 = new Video("Z Video macka", new ArrayList<>(Arrays.asList(category1)));
       video1.setId("1");
       Video video2 = new Video("D Video pas", new ArrayList<>(Arrays.asList(category2)));
       video2.setId("2");
       Video video5 = new Video("T Sjajan video macke kako pada u vodu", new ArrayList<>(Arrays.asList(categoryRepo.save(new Category("Horror")))));
       video5.setId("3");
       Video video6 = new Video("B Video kako se penje lik", new ArrayList<>(Arrays.asList(categoryRepo.save(new Category("Action")))));
        video6.setId("4");

       videoRepo.save(video1);
       videoRepo.save(video2);
       videoRepo.save(video5);
       videoRepo.save(video6);

        Playlist playlist1 = new Playlist("Playlist 1" , new ArrayList<>(Arrays.asList(category1)));
        playlist1.setId("1");

        playlistRepo.save(playlist1);

        VideoPlaylistOrder vpo1 = new VideoPlaylistOrder();
        vpo1.setPlaylist(playlist1);
        vpo1.setVideo(video1);
        vpo1.setOrderNumber(1);
        vpo1.setId("1");

        VideoPlaylistOrder vpo2 = new VideoPlaylistOrder();
        vpo2.setPlaylist(playlist1);
        vpo2.setVideo(video2);
        vpo2.setOrderNumber(2);
        vpo2.setId("2");

       VideoPlaylistOrder vpo3 = new VideoPlaylistOrder();
       vpo3.setPlaylist(playlist1);
       vpo3.setVideo(video5);
       vpo3.setOrderNumber(3);
       vpo3.setId("3");

       VideoPlaylistOrder vpo4 = new VideoPlaylistOrder();
       vpo4.setPlaylist(playlist1);
       vpo4.setVideo(video6);
       vpo4.setOrderNumber(4);
       vpo4.setId("4");


       videoPlaylistOrderRepo.save(vpo1);
        videoPlaylistOrderRepo.save(vpo2);
        videoPlaylistOrderRepo.save(vpo3);
        videoPlaylistOrderRepo.save(vpo4);

   }

   @Test
    public void videoGetsRemovedFromPlaylistTest(){
        Optional<VideoPlaylistOrder> deletedItem = videoPlaylistOrderRepo.getVideoPlaylistOrderByPlaylistAndVideo(playlistRepo.getById("1"), videoRepo.getById("1"));
       playlistVideoService.removeVideoFromPlaylist("1", "1");
       playlistVideoService.removeVideoFromPlaylist("1", "3");
       List<VideoPlaylistOrder> videoPlaylistOrders = videoPlaylistOrderRepo.getVideoPlaylistOrdersByPlaylist(playlistRepo.getById("1"));


       assertThat(videoPlaylistOrders).doesNotContain(deletedItem.get());
       assertThat(videoPlaylistOrders).size().isEqualTo(1);

   }

   @Test

    public void changeVideoIndexTest(){
       List<VideoPlaylistOrder> playlist = playlistVideoService.videoIndex("1", "1", 3);
       VideoPlaylistOrder vpo = videoPlaylistOrderRepo.getVideoPlaylistOrderByPlaylistAndVideo(playlistRepo.getById("1"), videoRepo.getById("1")).get();
        Integer indexOfElement = playlist.indexOf(vpo);
       assertThat(playlist.get(indexOfElement).getOrderNumber()).isEqualTo(3);
       assertThat(playlist.get(indexOfElement + 1).getOrderNumber()).isEqualTo(1);
       assertThat(playlist.get(indexOfElement + 2).getOrderNumber()).isEqualTo(2);


   }

}
