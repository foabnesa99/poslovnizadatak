package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.model.Video;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

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

    public VideoController(VideoService videoService, SessionLoggedUserHandler userHandler, PlaylistVideoService playlistVideoService) {
        this.videoService = videoService;
        this.userHandler = userHandler;
        this.playlistVideoService = playlistVideoService;
    }

    @ApiOperation(value = "Add a new video", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created"),
            @ApiResponse(code = 500, message = "Internal server error"),
    }
    )

    @PostMapping(value="/add", consumes="application/json")
    public ResponseEntity<Video> saveVideo(@RequestBody Video video) {
        try {
            videoService.save(video);
            return new ResponseEntity<>(video, HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @ApiOperation(value = "Get a list of all videos", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful | OK")
    }
    )
    @GetMapping(value = "/")
    public ModelAndView getVideos() {
        try{
            User user = userHandler.getUser(SecurityContextHolder.getContext());
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

    @ApiOperation(value = "Get a single video", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 404, message="Not found"),
            @ApiResponse(code = 500, message = "Internal server error"),
    })
    @GetMapping(value = "/{id}")
    public ResponseEntity<Video> getVideo(@PathVariable("id") Integer id){
        try {
            Video video = videoService.findOne(id.toString());
            return new ResponseEntity<>(video, HttpStatus.OK);

        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
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
