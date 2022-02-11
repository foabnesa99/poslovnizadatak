package com.example.demo.controller;

import com.example.demo.model.Playlist;
import com.example.demo.repo.PlaylistRepo;
import com.example.demo.service.PlaylistVideoService;
import com.example.demo.util.exceptions.ResourceMissingException;
import org.jboss.jandex.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "api/playlists")
public class PlaylistController {

    @Autowired
    PlaylistVideoService playlistVideoService;

    @Autowired
    PlaylistRepo playlistRepo;

    @RequestMapping(path = "{id}/sort", method = RequestMethod.PUT)
    public ResponseEntity playlistSort(@PathVariable String id) {
        try {
            Playlist playlist = playlistVideoService.videoSort(id);
            return new ResponseEntity<Playlist>(playlist, HttpStatus.OK);
        } catch (ResourceMissingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @RequestMapping(path = "{id}/delete/{videoid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeVideo(@PathVariable int id, @PathVariable int videoid) {
        try {
            playlistVideoService.removeVideo(Integer.toString(id), Integer.toString(videoid));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceMissingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @RequestMapping(path = "{id}/add/{videoid}", method = RequestMethod.PATCH)
    public ResponseEntity<?> addVideo(@PathVariable int id, @PathVariable int videoid) {

        try {
            Playlist playlist = playlistVideoService.addVideo(Integer.toString(id), Integer.toString(videoid));
            return new ResponseEntity<Playlist>(playlist, HttpStatus.OK);
        } catch (ResourceMissingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @RequestMapping(path = "{id}/position/{currentIndex}/to/{newIndex}", method = RequestMethod.PATCH)
    public ResponseEntity<?> positionUpdate(@PathVariable int id, @PathVariable int currentIndex, @PathVariable int newIndex) {

        try {
            Playlist playlist = playlistVideoService.videoIndex(Integer.toString(id), currentIndex, newIndex);
            return new ResponseEntity<Playlist>(playlist, HttpStatus.OK);
        } catch (ResourceMissingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        catch (IndexOutOfBoundsException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);

        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
}
