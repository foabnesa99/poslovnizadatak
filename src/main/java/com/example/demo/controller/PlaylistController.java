package com.example.demo.controller;

import com.example.demo.model.*;
import com.example.demo.service.*;
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
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.websocket.server.PathParam;
import java.util.ArrayList;
import java.util.HashSet;
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

   private final ChannelService channelService;


    public PlaylistController(SessionLoggedUserHandler userHandler, PlaylistVideoService playlistVideoService, ChannelPlaylistService channelPlaylistService, VideoService videoService, PlaylistService playlistService, ChannelService channelService) {
        this.userHandler = userHandler;
        this.playlistVideoService = playlistVideoService;
        this.channelPlaylistService = channelPlaylistService;
        this.videoService = videoService;
        this.playlistService = playlistService;
        this.channelService = channelService;
    }

    @ApiOperation(value = "Get a list of all playlists", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful | OK")
    }
    )
    @GetMapping(value = "/")
    public ModelAndView getPlaylists() {
        User user = userHandler.getUser();
        Channel channel = channelService.getChannelByUser(user);
        PlaylistChannel pcl = new PlaylistChannel();
        ModelAndView mav = new ModelAndView("playlists");
        if (user.getRoles().toString() == "ROLE_USER"){
            List<Playlist> playlistList = channelPlaylistService.findPlaylistsForUser(user);
            log.info("Obtaining playlists for logged-in user...  " + playlistList.size());
            mav.addObject("playlists", playlistList);
            mav.addObject("channel", channel);
            mav.addObject("playlistchannel", pcl);
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

    @GetMapping(value = "/add")
    public ModelAndView newPlaylist(){
        ModelAndView mav = new ModelAndView("newPlaylist");
        mav.addObject(new Playlist());
        return mav;
    }

    @PostMapping(value="/add", consumes="application/x-www-form-urlencoded")
    public RedirectView savePlaylist(Playlist playlist) {
        try {
            User user = userHandler.getUser();
            Channel channel = channelService.getChannelByUser(user);
            playlist.setCategories(new HashSet<>());
            playlist.setImageSrc("http://simpleicon.com/wp-content/uploads/playlist.png");
            playlistService.save(playlist);
            PlaylistChannel pcl = channelPlaylistService.addPlaylistToChannel(channel.getId(), playlist.getId());

        } catch (Exception e) {
            log.info(e.getMessage());

        }
        return new RedirectView("/api/playlists/", true);
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
            VideoPlaylist vpl = new VideoPlaylist();
            Playlist playlist = playlistService.getPlaylist(Integer.toString(id));
            List<Video> videosList = playlistVideoService.videosInPlaylist(playlist);
            ModelAndView mav = new ModelAndView("singlePlaylist");
            mav.addObject("videos", videosList);
            mav.addObject("playlist", playlist);
            mav.addObject("videoplaylist", vpl);
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
    public RedirectView removeVideo(@PathVariable int playlistid, @PathVariable int videoid) {
        try {
            playlistVideoService.removeVideoFromPlaylist(Integer.toString(playlistid), Integer.toString(videoid));

        } catch (ResourceMissingException e) {
           log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());

        }
        return new RedirectView("/api/playlists/" + playlistid, true);
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
    @RequestMapping(path = "{playlistid}/videos/{videoId}/to/", method = RequestMethod.POST)
    public RedirectView positionUpdate(@PathVariable int playlistid, @PathVariable int videoId, @RequestParam String orderNumber) {
        try {
            List<VideoPlaylist> playlist = playlistVideoService.videoIndex(Integer.toString(playlistid), Integer.toString(videoId), Integer.valueOf(orderNumber));
        } catch (ResourceMissingException e) {
            log.error(e.getMessage());
        }
       catch (IndexOutOfBoundsException e) {
           log.error(e.getMessage());

        }
        catch (Exception e) {
            log.error(e.getMessage());
        }
        return new RedirectView("/api/playlists/" + playlistid, true);
    }
}
