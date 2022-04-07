package com.example.demo.service.imp;

import com.example.demo.model.Playlist;
import com.example.demo.model.User;
import com.example.demo.model.Video;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.UserService;
import com.example.demo.util.exceptions.ResourceMissingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    UserRepo userRepo;

    @Override
    public User findOne(String userId) {
        Optional<User> user = userRepo.findById(userId);
        if (user.isEmpty()) {
            throw new ResourceMissingException();
        }
        return user.get();
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public User save(User user) {
        return userRepo.save(user);
    }

    @Override
    public void remove(String userId) {
        userRepo.deleteById(userId);

    }
}
