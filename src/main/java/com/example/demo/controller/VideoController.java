package com.example.demo.controller;


import com.example.demo.model.*;
import com.example.demo.service.*;
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

    private final PlaylistService playlistService;

    private final CategoryService categoryService;

    public VideoController(VideoService videoService, SessionLoggedUserHandler userHandler, PlaylistVideoService playlistVideoService, ChannelPlaylistService channelPlaylistService, PlaylistService playlistService, CategoryService categoryService) {
        this.videoService = videoService;
        this.userHandler = userHandler;
        this.playlistVideoService = playlistVideoService;
        this.channelPlaylistService = channelPlaylistService;
        this.playlistService = playlistService;
        this.categoryService = categoryService;
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
            if(user.getRoles().toString() == "ROLE_USER") {
                Set<Video> userVideos = playlistVideoService.videosInUserPlaylists(user);
                log.info("Fetching a list of videos that are in the user's playlists...");
                mav.addObject("uservideos", userVideos);
            }
            List<Video> allVideos = videoService.findAll();
            log.info("Fetching all videos...");
            mav.addObject("allvideos", allVideos);
            return mav;

        }
        catch (ResourceMissingException e){
            return new ModelAndView("missingresource");
        }
    }

    @GetMapping(value = "/add")
    public ModelAndView newVideo(){
        List<Category> categories = categoryService.findAll();
        ModelAndView mav = new ModelAndView("addVideo");
        mav.addObject("categories", categories);
        mav.addObject(new Video());
        return mav;
    }
    @PostMapping(value="/add", consumes="application/x-www-form-urlencoded")
    public RedirectView saveVideo(Video video) {
        try {
            log.info("Saving the video...");
            videoService.save(video);
        } catch (Exception e) {
            log.info(e.getMessage());

        }
        return new RedirectView("/api/videos/", true);
    }

    @GetMapping(value = "/edit/{id}")
    public ModelAndView editVideo(@PathVariable Integer id){
        ModelAndView mav = new ModelAndView("editVideo");
        Video video = videoService.getVideo(String.valueOf(id));
        List<Category> categories = categoryService.findAll();
        mav.addObject("categories" , categories);
        mav.addObject("video", video);
        return mav;
    }

    @ApiOperation(value = "Editing a video", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful | OK"),
            @ApiResponse(code = 404, message="Not found"),
            @ApiResponse(code = 500, message = "Internal server error"),
    }
    )

    @PostMapping (value="/edit/{id}", consumes="application/x-www-form-urlencoded")
    public RedirectView updateVideo(Video videoUpdate, @PathVariable("id") Integer id) {
        try {
            Video video = videoService.getVideo(String.valueOf(id));
            video.setCategories(videoUpdate.getCategories());
            video.setUrl(videoUpdate.getUrl());
            video.setName(videoUpdate.getName());
            videoService.save(video);

        } catch (Exception e) {
            log.error(e.getMessage());

        }
        return new RedirectView("/api/videos/" + id, true);
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
            ModelAndView mav = new ModelAndView("singleVideo");
            if(user.getRoles().toString() == "ROLE_USER"){
                List<Playlist> userPlaylists = channelPlaylistService.findPlaylistsForUser(user);
                mav.addObject("playlists", userPlaylists);
            } else if (user.getRoles().toString() == "ROLE_ADMIN") {
                List<Playlist> userPlaylists = playlistService.findAll();
                mav.addObject("playlists", userPlaylists);
            }

            Video video = videoService.getVideo(String.valueOf(id));
            Playlist selectedPlaylist = new Playlist();

            mav.addObject("video", video);
            mav.addObject("selectedPlaylist", selectedPlaylist);

            return mav;

        }
        catch (Exception e){
            log.error(e.getMessage());
            return new ModelAndView("missingresource");
        }


    }





    @ApiOperation(value = "Removing a video", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 404, message = "Not found"),
    }
    )
    @DeleteMapping(value="/{id}")
    public RedirectView deleteVideo(@PathVariable("id") Integer id){

        try {
            Video video = videoService.findOne(id.toString());
            playlistVideoService.removeVideo(video.getId());

        }catch (ResourceMissingException e){
            log.error(e.getMessage());
        }
        return new RedirectView("/api/videos/", true);
    }

}
