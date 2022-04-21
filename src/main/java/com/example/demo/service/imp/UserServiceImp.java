package com.example.demo.service.imp;

import com.example.demo.model.Playlist;
import com.example.demo.model.User;
import com.example.demo.model.Video;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.UserService;
import com.example.demo.util.exceptions.MissingUserException;
import com.example.demo.util.exceptions.ResourceMissingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Slf4j
@Transactional
public class UserServiceImp implements UserService, UserDetailsService {

    @Autowired
    PasswordEncoder passwordEncoder;
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
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepo.save(user);
    }

    @Override
    public void remove(String userId) {
        log.info("Removing the user from the database...");
        userRepo.deleteById(userId);

    }

    @Override
    public User findByUsername(String username) {
        List<User> foundUsers = userRepo.findAll();
        System.out.println("\n \n \n" + foundUsers + "\n KORISNICI \n \n");
        Optional<User> foundUser = userRepo.findUserByUsername(username);
        if (foundUser.isEmpty())throw new MissingUserException();
        return foundUser.get();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = findByUsername(username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(user.getRoles().toString()));
        System.out.println(authorities + "AUTHORITIES OBJ \n \n");
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }
}
