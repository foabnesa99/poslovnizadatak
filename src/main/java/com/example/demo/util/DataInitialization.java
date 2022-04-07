package com.example.demo.util;

import com.example.demo.model.*;
import com.example.demo.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitialization implements ApplicationRunner {

    private UserRepo userRepository;
    private PlaylistRepo playlistRepo;
    private VideoRepo videoRepo;
    private CategoryRepo categoryRepo;
    private ChannelRepo channelRepo;

    @Autowired
    public DataInitialization(UserRepo userRepository, PlaylistRepo playlistRepo, VideoRepo videoRepo, CategoryRepo categoryRepo, ChannelRepo channelRepo) {
        this.userRepository = userRepository;
        this.playlistRepo = playlistRepo;
        this.videoRepo = videoRepo;
        this.categoryRepo = categoryRepo;
        this.channelRepo = channelRepo;
    }

    public void run(ApplicationArguments args) {

        Playlist plejlista1 = new Playlist();
        Playlist plejlista2 = new Playlist();
        Playlist plejlista3 = new Playlist();

        Category category1 = new Category("Thriller");
        Category category2 = new Category( "Comedy");
        Category category3 = new Category("Action");
        Category category4 = new Category("Horror");

        categoryRepo.save(category1);
        categoryRepo.save(category2);
        categoryRepo.save(category3);
        categoryRepo.save(category4);

        List<Category> categories = new ArrayList<>();
        List<Category> categories4 = new ArrayList<Category>(Arrays.asList(category4));
        categories.add(category1);
        categories.add(category2);

        List<Category> categories2 = new ArrayList<>();

        categories2.add(category3);

        List<Category> categories3 = new ArrayList<>();
        categories3.add(category2);

        Video video1 = new Video("Z Video macka", categories);
        Video video2 = new Video("D Video pas", categories2);
        Video video3 = new Video("E Video petao", categories4);
        //Video video4 = new Video("4", "C Video gameplay", new ArrayList<Category>(Arrays.asList(category1, category3)));



        videoRepo.save(video1);
        videoRepo.save(video2);
        videoRepo.save(video3);
        //videoRepo.save(video4);


        List<Video> plejlistaVideo = new ArrayList<Video>();
        plejlistaVideo.add(video1);
        plejlistaVideo.add(video2);
        plejlistaVideo.add(video3);

        plejlista1.setVideoList(plejlistaVideo);
        plejlista1.setName("Perina prva plejlista");
        plejlista1.setCategories(new ArrayList<>(Arrays.asList(category1)));
        playlistRepo.save(plejlista1);

        List<Playlist> plejlistakorisnik1 = new ArrayList<Playlist>();
        plejlistakorisnik1.add(plejlista1);

        Channel channel1 = new Channel();

        User korisnik1 = new User("Petar Petrovic",plejlistakorisnik1, channel1);
        channel1.setId("1");
        channel1.setPlaylistList(plejlistakorisnik1);
        channelRepo.save(channel1);
        userRepository.save(korisnik1);

        plejlista1.setUser(korisnik1);
        playlistRepo.save(plejlista1);
    }
}