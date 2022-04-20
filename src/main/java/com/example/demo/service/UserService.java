package com.example.demo.service;

import com.example.demo.model.User;

import java.util.List;

public interface UserService {

    User findOne(String userId);

    List<User> findAll();

    User save(User user);

    void remove(String userId);

    User findByUsername(String username);

}
