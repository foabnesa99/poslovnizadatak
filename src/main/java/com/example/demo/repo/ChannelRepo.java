package com.example.demo.repo;

import com.example.demo.model.Channel;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelRepo extends JpaRepository<Channel, String> {

    Optional<Channel> getChannelByUser(User user);
}
