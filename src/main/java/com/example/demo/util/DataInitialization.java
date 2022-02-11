package com.example.demo.util;

import com.example.demo.model.Playlist;
import com.example.demo.model.User;
import com.example.demo.model.Video;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.repo.UserRepo;
import com.example.demo.repo.VideoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataInitialization implements ApplicationRunner {

    private UserRepo userRepository;
    private PlaylistRepo playlistRepo;
    private VideoRepo videoRepo;

    @Autowired
    public DataInitialization(UserRepo userRepository, PlaylistRepo playlistRepo, VideoRepo videoRepo) {
        this.userRepository = userRepository;
        this.playlistRepo = playlistRepo;
        this.videoRepo = videoRepo;
    }

    public void run(ApplicationArguments args) {

        Playlist plejlista1 = new Playlist();
        Playlist plejlista2 = new Playlist();
        Playlist plejlista3 = new Playlist();

        Video video1 = new Video("1", "A Video macka");
        Video video2 = new Video("2", "D Video pas");
        Video video3 = new Video("3", "E Video petao");
        Video video4 = new Video("4", "C Video gameplay");
        Video video5 = new Video("5", "F Video podvodni");
        Video video6 = new Video("6", "B Video suma");


        videoRepo.save(video1);
        videoRepo.save(video2);
        videoRepo.save(video3);
        videoRepo.save(video4);
        videoRepo.save(video5);
        videoRepo.save(video6);

        List<Video> plejlistaVideo = new ArrayList<Video>();
        plejlistaVideo.add(video1);
        plejlistaVideo.add(video2);
        plejlistaVideo.add(video3);

        plejlista1.setVideoList(plejlistaVideo);
        plejlista1.setName("Perina prva plejlista");
        playlistRepo.save(plejlista1);

        List<Playlist> plejlistakorisnik1 = new ArrayList<Playlist>();
        plejlistakorisnik1.add(plejlista1);
        User korisnik1 = new User("1", "Petar Petrovic",plejlistakorisnik1);
        userRepository.save(korisnik1);

        plejlista1.setUser(korisnik1);
        playlistRepo.save(plejlista1);
    }
}