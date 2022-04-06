package com.example.demo.service.imp;

import com.example.demo.model.Video;
import com.example.demo.repo.VideoRepo;
import com.example.demo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class VideoServiceImp implements VideoService {

    @Autowired
    VideoRepo videoRepo;

    @Override
    public Video findOne(String videoId) {
        return videoRepo.getById(videoId);
    }

    @Override
    public List<Video> findAll() {
        return videoRepo.findAll();
    }

    @Override
    public Video save(Video video) {
        return videoRepo.save(video);
    }

    @Override
    public void remove(String videoId) {
        videoRepo.deleteById(videoId);
    }
}
