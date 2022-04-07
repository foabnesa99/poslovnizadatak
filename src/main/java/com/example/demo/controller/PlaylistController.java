package com.example.demo.controller;

import com.example.demo.model.Category;
import com.example.demo.model.Playlist;
import com.example.demo.model.VideoPlaylistOrder;
import com.example.demo.service.PlaylistVideoService;
import com.example.demo.util.exceptions.ResourceMissingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Playlist Rest Controller", description = "REST API for playlists")
@RestController
@RequestMapping(value = "/api/playlists")
public class PlaylistController {


   private PlaylistVideoService playlistVideoService;


    public PlaylistController(PlaylistVideoService playlistVideoService) {
        this.playlistVideoService = playlistVideoService;
    }

    @ApiOperation(value = "Get a list of all playlists", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful | OK")
    }
    )
    @GetMapping(value = "/")
    public ResponseEntity<List<Playlist>> getPlaylists() {
        List<Playlist> playlistList = playlistVideoService.findAll();
        return new ResponseEntity<>(playlistList, HttpStatus.OK);
    }


    @RequestMapping(path = "/{playlistid}/sort", method = RequestMethod.PUT)
    @ApiOperation(value = "Sort a playlist ", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not Found!") })
    public ResponseEntity playlistSort(@PathVariable String playlistid) {
        try {
            List<VideoPlaylistOrder> playlist = playlistVideoService.videoSort(playlistid);
            return new ResponseEntity<>(playlist, HttpStatus.OK);
        } catch (ResourceMissingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } /*catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }*/

    }
    @ApiOperation(value = "Removing a video from a playlist", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not Found!") })
    @RequestMapping(path = "/{playlistid}/videos/delete/{videoid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeVideo(@PathVariable int playlistid, @PathVariable int videoid) {
        try {
            playlistVideoService.removeVideo(Integer.toString(playlistid), Integer.toString(videoid));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceMissingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @ApiOperation(value = "Add a video to a playlist", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not Found!") })
    @RequestMapping(path = "/{playlistid}/videos/add/{videoid}", method = RequestMethod.PATCH)
    public ResponseEntity<?> addVideo(@PathVariable int playlistid, @PathVariable int videoid) {

        try {
            Playlist playlist = playlistVideoService.addVideo(Integer.toString(playlistid), Integer.toString(videoid));
            return new ResponseEntity<Playlist>(playlist, HttpStatus.OK);
        } catch (ResourceMissingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @ApiOperation(value = "Change the position of a video in a playlist", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 403, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not Found!") })
    @RequestMapping(path = "{playlistid}/videos/{currentIndex}/to/{newIndex}", method = RequestMethod.PATCH)
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
