package com.example.demo.repo;

import com.example.demo.model.VideoPlaylistOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoPlaylistOrderRepo extends JpaRepository<VideoPlaylistOrder, String> {
}
