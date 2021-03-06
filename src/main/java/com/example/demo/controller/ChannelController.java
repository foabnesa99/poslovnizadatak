package com.example.demo.controller;


import com.example.demo.model.Category;
import com.example.demo.model.Channel;
import com.example.demo.model.PlaylistChannel;
import com.example.demo.model.User;
import com.example.demo.repo.UserRepo;
import com.example.demo.service.ChannelPlaylistService;
import com.example.demo.service.ChannelService;
import com.example.demo.util.SessionLoggedUserHandler;
import com.example.demo.util.exceptions.ChannelMissingException;
import com.example.demo.util.exceptions.PlaylistMissingException;
import com.example.demo.util.exceptions.PlaylistNotInChannelException;
import com.example.demo.util.exceptions.ResourceMissingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RScript;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Api(value = "Channel Rest Controller", description = "REST API for channels")
@RestController
@RequestMapping(value = "/api/channels/")
@Slf4j
public class ChannelController {

    private final ChannelService channelService;

    private final UserRepo userRepo;

    private final ChannelPlaylistService channelPlaylistService;

    private final SessionLoggedUserHandler userHandler;

    public ChannelController(ChannelService channelService, UserRepo userRepo, ChannelPlaylistService channelPlaylistService, SessionLoggedUserHandler userHandler) {
        this.channelService = channelService;
        this.userRepo = userRepo;
        this.channelPlaylistService = channelPlaylistService;
        this.userHandler = userHandler;
    }

    @ApiOperation(value = "Get a list of all channels", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful | OK")
    }
    )
    @GetMapping(value = "/")
    public ModelAndView getChannels() {
        User user = userHandler.getUser();
        ModelAndView mav = new ModelAndView("channels");
        if (user.getRoles().toString() == "ROLE_USER") {
            log.info("Obtaining channel for logged-in user...");
            Channel channel = channelService.getChannelByUser(user);
            mav.addObject("channel", channel);
        } else if (user.getRoles().toString() == "ROLE_ADMIN") {
            log.info("Admin logged in - obtaining all channels...");
            List<Channel> channelList = channelService.findAll();
            mav.addObject("channel", channelList);
        } else {
            log.info("User not logged in");
        }
        return mav;

    }

    @GetMapping(value = "/edit/{id}")
    public ModelAndView editChannel(@PathVariable Integer id){
        ModelAndView mav = new ModelAndView("updateChannelName");
        Channel channel = channelService.getChannel(String.valueOf(id));
        mav.addObject("channel", channel);
        return mav;
    }

    @ApiOperation(value = "Editing a channel", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful | OK"),
            @ApiResponse(code = 404, message="Not found"),
            @ApiResponse(code = 500, message = "Internal server error"),
    }
    )

    @PostMapping (value="/edit/{id}", consumes="application/x-www-form-urlencoded")
    public RedirectView updateChannel(Channel editChannel, @PathVariable("id") Integer id) {
        try {
            Channel channel = channelService.getChannel(String.valueOf(id));
            channel.setName(editChannel.getName());
            channelService.save(channel);

        } catch (Exception e) {
            log.error(e.getMessage());

        }
        return new RedirectView("/api/channels/");
    }

    @ApiOperation(value = "Removing a channel", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 404, message = "Not found"),
    }
    )
    @DeleteMapping(value="/{id}")
    public RedirectView deleteChannel(@PathVariable("id") Integer id){

        try {
            channelPlaylistService.deleteChannel(String.valueOf(id));

        }catch (ResourceMissingException e){
            log.error(e.getMessage());
        }
        return new RedirectView("/api/channels/", true);
    }


    @ApiOperation(value = "Removing a playlist from a channel", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not Found!")})
    @RequestMapping(path = "/{channelid}/playlists/delete/{playlistid}", method = RequestMethod.DELETE)
    public RedirectView removePlaylist(@PathVariable int channelid, @PathVariable int playlistid) {
        try {
            channelPlaylistService.removePlaylistFromChannel(Integer.toString(channelid), Integer.toString(playlistid));

        } catch (ResourceMissingException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new RedirectView("/api/playlists/", true);
    }

    @ApiOperation(value = "Add a playlist to a channel", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not Found!")})
    @RequestMapping(path = "/{channelid}/playlists/add/{playlistid}", method = RequestMethod.PATCH)
    public ResponseEntity<?> addPlaylist(@PathVariable int channelid, @PathVariable int playlistid) {

        try {
            PlaylistChannel playlistChannel = channelPlaylistService.addPlaylistToChannel(Integer.toString(channelid), Integer.toString(playlistid));
            return new ResponseEntity<>(playlistChannel, HttpStatus.OK);
        } catch (PlaylistNotInChannelException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @RequestMapping(path = "/{channelid}/sort", method = RequestMethod.PUT)
    @ApiOperation(value = "Sort the playlists of a channel ", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not Found!")})
    public ResponseEntity<?> channelPlaylistSort(@PathVariable String channelid) {
        try {
            List<PlaylistChannel> channels = channelPlaylistService.playlistSort(channelid);
            return new ResponseEntity<>(channels, HttpStatus.OK);
        } catch (ChannelMissingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ApiOperation(value = "Change the position of a playlist in a channel", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not Found!")})
    @RequestMapping(path = "{channelid}/playlists/{playlistid}/to/", method = RequestMethod.POST)
    public RedirectView positionUpdate(@PathVariable int channelid, @PathVariable int playlistid, @RequestParam String orderNumber) {
        try {
            List<PlaylistChannel> channels = channelPlaylistService.playlistIndex(Integer.toString(channelid), Integer.toString(playlistid), Integer.valueOf(orderNumber));

        } catch (ResourceMissingException e) {
            log.error(e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new RedirectView("/api/playlists/", true);
    }


}
