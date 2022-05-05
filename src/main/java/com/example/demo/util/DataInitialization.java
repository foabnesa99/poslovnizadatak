package com.example.demo.util;

import com.example.demo.model.*;
import com.example.demo.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Profile("prod")
public class DataInitialization implements ApplicationRunner {

    private PasswordEncoder passwordEncoder;
    private UserRepo userRepository;
    private PlaylistRepo playlistRepo;
    private VideoRepo videoRepo;
    private CategoryRepo categoryRepo;
    private ChannelRepo channelRepo;

    private ChannelPlaylistRepo channelPlaylistRepo;

    private VideoPlaylistRepo videoPlaylistRepo;

    public DataInitialization(PasswordEncoder passwordEncoder, UserRepo userRepository, PlaylistRepo playlistRepo, VideoRepo videoRepo, CategoryRepo categoryRepo, ChannelRepo channelRepo, ChannelPlaylistRepo channelPlaylistRepo, VideoPlaylistRepo videoPlaylistRepo) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.playlistRepo = playlistRepo;
        this.videoRepo = videoRepo;
        this.categoryRepo = categoryRepo;
        this.channelRepo = channelRepo;
        this.channelPlaylistRepo = channelPlaylistRepo;
        this.videoPlaylistRepo = videoPlaylistRepo;
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

        Set<Category> categories = new HashSet<>();
        Set<Category> categories4 = new HashSet<Category>(Arrays.asList(category4));
        categories.add(category1);
        categories.add(category2);

        Set<Category> categories2 = new HashSet<>();

        categories2.add(category3);

        Set<Category> categories3 = new HashSet<>();
        categories3.add(category2);

        Video video1 = new Video("Drzavni Posao 1 - Rakija" , "https://www.youtube.com/embed/McwPB-eQ2BY",categories);
        video1.setVideoUriId("McwPB-eQ2BY");
        Video video2 = new Video("D Video pas","https://www.youtube.com/embed/kRS5A_j5ugk" ,categories2);
        video2.setVideoUriId("kRS5A_j5ugk");
        Video video3 = new Video("E Video petao","https://www.youtube.com/embed/6QOh6TXn-kI" ,categories4);
        video3.setVideoUriId("6QOh6TXn-kI");
        Video video4 = new Video("C Video gameplay", "https://www.youtube.com/embed/iNzfKn4bnMM",new HashSet<>(Arrays.asList(category2, category3)));
        video4.setVideoUriId("iNzfKn4bnMM");
        Video video5 = new Video("T Sjajan video macke kako pada u vodu","https://www.youtube.com/embed/PMNYG5S9LnM" ,new HashSet<>(Arrays.asList(category1, category3)));
        video5.setVideoUriId("PMNYG5S9LnM");
        Video video6 = new Video("B Video kako se penje lik","https://www.youtube.com/embed/2dix67H69v8" ,new HashSet<>(Arrays.asList(category1, category4)));
        video6.setVideoUriId("2dix67H69v8");

        System.out.println("\n \n \n VIDEO 1 TEST" + video1);


        videoRepo.save(video1);
        videoRepo.save(video2);
        videoRepo.save(video3);
        videoRepo.save(video4);
        videoRepo.save(video5);
        videoRepo.save(video6);


        plejlista1.setName("Perina prva plejlista");
        plejlista1.setCategories(new HashSet<>());
        plejlista1.setImageSrc("http://simpleicon.com/wp-content/uploads/playlist.png");
        playlistRepo.save(plejlista1);

        plejlista2.setName("Perina druga plejlista");
        plejlista2.setCategories(new HashSet<>());
        plejlista2.setImageSrc("http://simpleicon.com/wp-content/uploads/playlist.png");
        playlistRepo.save(plejlista2);

        plejlista3.setName("Ovo je random plejlista");
        plejlista3.setCategories(new HashSet<>());
        plejlista3.setImageSrc("http://simpleicon.com/wp-content/uploads/playlist.png");
        playlistRepo.save(plejlista3);

        Channel channel1 = new Channel();
        channel1.setName("Perin Super Awesome kanal");
        Channel channel2 = new Channel();
        channel2.setName("Dragcetov gameplay");

        User korisnik1 = new User("Petar Petrovic" , "pera1", passwordEncoder.encode("peracar011"), UserRoles.ROLE_USER);
        User korisnik2 = new User("Dragan Milovanovic", "draganche", passwordEncoder.encode("12345678"), UserRoles.ROLE_USER);
        User korisnik3 = new User("Milan Admin" , "admin1" , passwordEncoder.encode("adminadmin") , UserRoles.ROLE_ADMIN);

        channel1.setUser(korisnik1);
        channel2.setUser(korisnik2);


        userRepository.save(korisnik1);
        userRepository.save(korisnik2);
        userRepository.save(korisnik3);
        channelRepo.save(channel2);
        channelRepo.save(channel1);

        channelPlaylistRepo.save(new PlaylistChannel(channel1, plejlista1, 1));
        channelPlaylistRepo.save(new PlaylistChannel(channel1,plejlista2,2));

        videoPlaylistRepo.save(new VideoPlaylist("1", video1, plejlista1, 1));
        videoPlaylistRepo.save(new VideoPlaylist("2", video2, plejlista1, 2));

    }
}