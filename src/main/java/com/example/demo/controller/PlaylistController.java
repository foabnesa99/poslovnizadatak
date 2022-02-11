package com.example.demo.controller;

import com.example.demo.model.Playlist;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.service.PlaylistService;
import com.example.demo.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(value = "api/playlist")
public class PlaylistController {

    @Autowired
    PlaylistService playlistService;

    @Autowired
    PlaylistRepo playlistRepo;

    @RequestMapping(path = "sort/{id}", method = RequestMethod.GET)
    public ResponseEntity playlistSort(@PathVariable String id){
       Playlist plejlista = playlistService.VideoSort(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(path = "delete/{id}/{videoid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeVideo(@PathVariable int id, @PathVariable int videoid) {
        Optional<Playlist> plejlista = playlistRepo.findById(Integer.toString(id));
        if (plejlista != null) {
            playlistService.RemoveVideo(Integer.toString(id),Integer.toString(videoid));
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "add/{id}/{videoid}", method = RequestMethod.PATCH)
    public ResponseEntity<?> addVideo(@PathVariable int id, @PathVariable int videoid) {
        Optional<Playlist> plejlista = playlistRepo.findById(Integer.toString(id));
        if (plejlista != null) {
            playlistService.AddVideo(Integer.toString(id),Integer.toString(videoid));
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "position/{id}/{currentIndex}/{newIndex}", method = RequestMethod.PATCH)
    public ResponseEntity<?> positionUpdate(@PathVariable int id, @PathVariable int currentIndex, @PathVariable int newIndex) {
        Optional<Playlist> plejlista = playlistRepo.findById(Integer.toString(id));
        if (plejlista != null) {
            playlistService.VideoIndex(Integer.toString(id), currentIndex, newIndex);
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
