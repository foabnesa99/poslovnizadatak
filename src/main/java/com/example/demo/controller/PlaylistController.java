package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.ChannelPlaylistService;
import com.example.demo.service.PlaylistService;
import com.example.demo.service.PlaylistVideoService;
import com.example.demo.service.VideoService;
import com.example.demo.util.SessionLoggedUserHandler;
import com.example.demo.util.exceptions.PlaylistMissingException;
import com.example.demo.util.exceptions.ResourceMissingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScript;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Api(value = "Playlist Rest Controller", description = "REST API for playlists")
@RestController
@RequestMapping(value = "/api/playlists")
@Slf4j
public class PlaylistController {

    private final SessionLoggedUserHandler userHandler;
   private final PlaylistVideoService playlistVideoService;

   private final ChannelPlaylistService channelPlaylistService;

   private final VideoService videoService;

   private final PlaylistService playlistService;


    public PlaylistController(SessionLoggedUserHandler userHandler, PlaylistVideoService playlistVideoService, ChannelPlaylistService channelPlaylistService, VideoService videoService, PlaylistService playlistService) {
        this.userHandler = userHandler;
        this.playlistVideoService = playlistVideoService;
        this.channelPlaylistService = channelPlaylistService;
        this.videoService = videoService;
        this.playlistService = playlistService;
    }

    @ApiOperation(value = "Get a list of all playlists", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful | OK")
    }
    )
    @GetMapping(value = "/")
    public ModelAndView getPlaylists() {
        User user = userHandler.getUser(SecurityContextHolder.getContext());
        ModelAndView mav = new ModelAndView("playlists");
        if (user.getRoles().toString() == "ROLE_USER"){
            List<Playlist> playlistList = channelPlaylistService.findPlaylistsForUser(user);
            log.info("Obtaining playlists for logged-in user...  " + playlistList.size());
            mav.addObject("playlists", playlistList);
        }else if (user.getRoles().toString() == "ROLE_ADMIN"){
            log.info("Admin logged in - obtaining all playlists...");
            List<Playlist> playlistList = playlistService.findAll();
            mav.addObject("playlists", playlistList);
        }
        else {
            log.info("User not logged in");
        }
        return mav;
    }

    @ApiOperation(value = "Get a single playlist", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message="Not found"),
            @ApiResponse(code = 500, message = "Internal server error"),
    })
    @GetMapping(value = "/{id}")
    public ModelAndView getPlaylist(@PathVariable("id") Integer id){
        try {
            Playlist playlist = playlistService.getPlaylist(Integer.toString(id));
            List<Video> videosList = playlistVideoService.videosInPlaylist(playlist);
            ModelAndView mav = new ModelAndView("singlePlaylist");
            mav.addObject("videos", videosList);
            mav.addObject("playlist", playlist);
            return mav;
        }
        catch (Exception e){
            return new ModelAndView("missingresource");
        }


    }


    @RequestMapping(path = "/{playlistid}/sort", method = RequestMethod.PUT)
    @ApiOperation(value = "Sort a playlist ", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not Found!") })
    public ResponseEntity playlistSort(@PathVariable String playlistid) {
        try {
            List<VideoPlaylist> playlist = playlistVideoService.videoSort(playlistid);
           return new ResponseEntity<>(playlist, HttpStatus.OK);
        }
        catch (PlaylistMissingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @ApiOperation(value = "Removing a video from a playlist", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not Found!") })
    @RequestMapping(path = "/{playlistid}/videos/delete/{videoid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeVideo(@PathVariable int playlistid, @PathVariable int videoid) {
        try {
            playlistVideoService.removeVideoFromPlaylist(Integer.toString(playlistid), Integer.toString(videoid));
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
            Playlist playlist = videoService.addVideoToPlaylist(Integer.toString(videoid), Integer.toString(playlistid));
            return new ResponseEntity<>(playlist, HttpStatus.OK);
        } catch (ResourceMissingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }


    @ApiOperation(value = "Change the position of a video in a playlist", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not Found!") })
    @RequestMapping(path = "{playlistid}/videos/{videoId}/to/{newIndex}", method = RequestMethod.PATCH)
    public ResponseEntity<?> positionUpdate(@PathVariable int playlistid, @PathVariable int videoId, @PathVariable int newIndex) {
        try {
            List<VideoPlaylist> playlist = playlistVideoService.videoIndex(Integer.toString(playlistid), Integer.toString(videoId), newIndex);
            return new ResponseEntity<>(playlist, HttpStatus.OK);
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
