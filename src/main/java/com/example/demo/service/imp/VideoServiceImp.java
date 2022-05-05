package com.example.demo.service.imp;

import com.example.demo.model.Category;
import com.example.demo.model.Playlist;
import com.example.demo.model.Video;
import com.example.demo.model.VideoPlaylist;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.repo.VideoPlaylistRepo;
import com.example.demo.repo.VideoRepo;
import com.example.demo.service.VideoService;
import com.example.demo.util.exceptions.PlaylistMissingException;
import com.example.demo.util.exceptions.VideoMissingException;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class VideoServiceImp implements VideoService {

    @Autowired
    VideoPlaylistRepo videoPlaylistRepo;

    @Autowired
    VideoRepo videoRepo;

    @Autowired
    PlaylistRepo playlistRepo;

    @Override
    public Video findOne(String videoId) {
        return videoRepo.getById(videoId);
    }

    @Override
    public Video getVideo(String videoId) {
        log.info("Retrieving a video with the requested id {}", videoId);
        Optional<Video> video = videoRepo.findById(videoId);
        if(video.isEmpty()) throw new VideoMissingException();
        return video.get();
    }

    @Override
    public List<Video> findAll() {
        log.info("Retrieving a list of all videos...");
        return videoRepo.findAll();
    }

    @Override
    public Video save(Video video) throws URISyntaxException {
        log.info("Saving the video to the database...");
        String[] parts = video.getUrl().split("/");
        log.info("\n \n VIDEO ID LIST" + Arrays.toString(parts));
        String videoID = parts[4];
        video.setCategories(new HashSet<>());
        video.setVideoUriId(videoID);
        log.info("Video saved " + video);
        return videoRepo.save(video);
    }

    @Override
    public void remove(String videoId) {
        log.info("Removing the video from the database...");
        videoRepo.deleteById(videoId);
    }

    @Override
    public Playlist addVideoToPlaylist(String videoId, String playlistId) {
        log.info("Adding the video {} to the requested playlist {}" , videoId, playlistId);
        VideoPlaylist videoPlaylist = new VideoPlaylist();
        Optional<Playlist> playlist = playlistRepo.findById(playlistId);
        videoPlaylist.setPlaylist(playlist.get());
        Optional<Video> video = videoRepo.findById(videoId);
        videoPlaylist.setVideo(video.get());

        List<VideoPlaylist> allVideosInPlaylists = videoPlaylistRepo.getVideoPlaylistsByPlaylist(playlist.get());

        if(allVideosInPlaylists.isEmpty()){
            videoPlaylist.setOrderNumber(1);
            log.info("Playlist is empty, adding the video to the first position");
        }
        else{
            videoPlaylist.setOrderNumber(allVideosInPlaylists.get(allVideosInPlaylists.size() - 1).getOrderNumber() + 1);
            log.info("Adding the video to the end of the playlist");
        }
        Set<Category> videoCategories = video.get().getCategories();
        videoCategories.forEach(category -> playlist.get().getCategories().add(category));
        playlistRepo.save(playlist.get());
        videoPlaylistRepo.save(videoPlaylist);
        log.info(videoPlaylist.toString() + "VPL OBJEKAT");
        log.info("Video successfully added!");
        return playlist.get();
    }
}
