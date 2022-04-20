package com.example.demo.service.imp;

import com.example.demo.model.Playlist;
import com.example.demo.model.User;
import com.example.demo.model.Video;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.UserService;
import com.example.demo.util.exceptions.ResourceMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImp implements UserService {

    @Autowired
    UserRepo userRepo;

    @Override
    public User findOne(String userId) {
        log.info("Retrieving a user with the requested id {}", userId);
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new ResourceMissingException();
        }
        return user.get();
    }

    @Override
    public List<User> findAll() {
        log.info("Retrieving a list of all users...");
        return userRepo.findAll();
    }

    @Override
    public User save(User user) {
        log.info("Saving the user to the database...");
        return userRepo.save(user);
    }

    @Override
    public void remove(String userId) {
        log.info("Removing the user from the database...");
        userRepo.deleteById(userId);

    }
}
