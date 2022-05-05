package com.example.demo.controller;


import com.example.demo.model.*;
import com.example.demo.service.ChannelPlaylistService;
import com.example.demo.service.PlaylistVideoService;
import com.example.demo.service.VideoService;
import com.example.demo.util.SessionLoggedUserHandler;
import com.example.demo.util.exceptions.ResourceMissingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Api(value = "Video Rest Controller", description = "REST API for videos")
@RestController
@RequestMapping(value = "/api/videos")
@Slf4j
public class VideoController {

    private final VideoService videoService;
    private final SessionLoggedUserHandler userHandler;
    private final PlaylistVideoService playlistVideoService;

    private final ChannelPlaylistService channelPlaylistService;

    public VideoController(VideoService videoService, SessionLoggedUserHandler userHandler, PlaylistVideoService playlistVideoService, ChannelPlaylistService channelPlaylistService) {
        this.videoService = videoService;
        this.userHandler = userHandler;
        this.playlistVideoService = playlistVideoService;
        this.channelPlaylistService = channelPlaylistService;
    }


    @ApiOperation(value = "Get a list of all videos", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful | OK")
    }
    )
    @GetMapping(value = "/")
    public ModelAndView getVideos() {
        try{
            User user = userHandler.getUser();
            ModelAndView mav = new ModelAndView("videos");
            Set<Video> userVideos = playlistVideoService.videosInUserPlaylists(user);
            log.info("Fetching a list of videos that are in the user's playlists...");
            List<Video> allVideos = videoService.findAll();
            log.info("Fetching all videos...");
            mav.addObject("uservideos", userVideos);
            mav.addObject("allvideos", allVideos);
            return mav;

        }
        catch (ResourceMissingException e){
            return new ModelAndView("missingresource");
        }
    }

    @GetMapping(value = "/add")
    public ModelAndView newVideo(){
        ModelAndView mav = new ModelAndView("addVideo");
        mav.addObject(new Video());
        return mav;
    }
    @PostMapping(value="/add", consumes="application/x-www-form-urlencoded")
    public RedirectView saveVideo(Video video) {
        try {
            videoService.save(video);
        } catch (Exception e) {
            log.info(e.getMessage());

        }
        return new RedirectView("/api/videos/", true);
    }

    @ApiOperation(value = "Add a video to a playlist", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 500, message = "Internal server error"),
    }
    )

    @PostMapping(value="/{id}/playlist/", consumes= MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public RedirectView addVideoToPlaylist(@PathVariable("id") Integer id, @ModelAttribute Playlist playlist) {
        try {
            log.info(playlist.toString() + " PLAYLIST FROM REQUEST   " + id + "  ID VIDEOA \n \n \n");
            videoService.addVideoToPlaylist(String.valueOf(id), playlist.getId());
            log.info("Video added to playlist");

        } catch (Exception e) {
            log.error(e.getMessage());

        }
        return new RedirectView("/api/videos/", true);

    }

    @ApiOperation(value = "Get a single video", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message="Not found"),
            @ApiResponse(code = 500, message = "Internal server error"),
    })
    @GetMapping(value = "/{id}")
    public ModelAndView getVideo(@PathVariable("id") Integer id){
        try {
            User user = userHandler.getUser();
            List<Playlist> userPlaylists = channelPlaylistService.findPlaylistsForUser(user);
            Video video = videoService.getVideo(String.valueOf(id));
            Playlist selectedPlaylist = new Playlist();
            ModelAndView mav = new ModelAndView("singleVideo");
            mav.addObject("video", video);
            mav.addObject("selectedPlaylist", selectedPlaylist);
            mav.addObject("playlists", userPlaylists);
            return mav;

        }
        catch (Exception e){
            return new ModelAndView("missingresource");
        }


    }




    @ApiOperation(value = "Editing a video", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful | OK"),
            @ApiResponse(code = 404, message="Not found"),
            @ApiResponse(code = 500, message = "Internal server error"),
    }
    )

    @PutMapping(value="/edit/{id}", consumes="application/json")
    public ResponseEntity<Video> updateVideo(@RequestBody Video editedVideo, @PathVariable("id") Integer id) {
        try {
            Video video = videoService.findOne(id.toString());
            video.setName(editedVideo.getName());
            video.setVideoPlaylists(editedVideo.getVideoPlaylists());
            video.setCategories(editedVideo.getCategories());
            return new ResponseEntity<>(video
                    , HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @ApiOperation(value = "Removing a video", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 404, message = "Not found"),
    }
    )
    @DeleteMapping(value="/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("id") Integer id){

        try {
            Video video = videoService.findOne(id.toString());
            videoService.remove(video.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (ResourceMissingException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
