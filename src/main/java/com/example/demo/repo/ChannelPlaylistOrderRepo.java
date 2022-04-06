package com.example.demo.repo;

import com.example.demo.model.ChannelPlaylistOrder;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelPlaylistOrderRepo extends JpaRepository<ChannelPlaylistOrder, String > {
}
