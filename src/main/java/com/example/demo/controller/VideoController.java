package com.example.demo.controller;


import com.example.demo.model.Video;
import com.example.demo.service.VideoService;
import com.example.demo.util.exceptions.ResourceMissingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Video Rest Controller", description = "REST API for videos")
@RestController
@RequestMapping(value = "/api/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
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
    public ResponseEntity<List<Video>> getVideos() {
        List<Video> videos = videoService.findAll();
        return new ResponseEntity<>(videos
                , HttpStatus.OK);
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
            video.setVideoPlaylistOrders(editedVideo.getVideoPlaylistOrders());
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
