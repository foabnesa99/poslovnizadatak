package com.example.demo.controller;


import com.example.demo.model.Channel;
import com.example.demo.model.PlaylistChannel;
import com.example.demo.service.ChannelPlaylistService;
import com.example.demo.service.ChannelService;
import com.example.demo.util.exceptions.ChannelMissingException;
import com.example.demo.util.exceptions.PlaylistMissingException;
import com.example.demo.util.exceptions.PlaylistNotInChannelException;
import com.example.demo.util.exceptions.ResourceMissingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "Channel Rest Controller", description = "REST API for channels")
@RestController
@RequestMapping(value = "/api/channels/")
public class ChannelController {

    private final ChannelService channelService;

    private final ChannelPlaylistService channelPlaylistService;

    public ChannelController(ChannelService channelService, ChannelPlaylistService channelPlaylistService) {
        this.channelService = channelService;
        this.channelPlaylistService = channelPlaylistService;
    }

    @ApiOperation(value = "Get a list of all channels", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successful | OK")
    }
    )
    @GetMapping(value = "/")
    public ResponseEntity<List<Channel>> getChannels() {
        List<Channel> channelList = channelService.findAll();
        return new ResponseEntity<>(channelList, HttpStatus.OK);
    }

    @ApiOperation(value = "Removing a playlist from a channel", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not Found!") })
    @RequestMapping(path = "/{channelid}/playlists/delete/{playlistid}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removePlaylist(@PathVariable int channelid, @PathVariable int playlistid) {
        try {
            channelPlaylistService.removePlaylistFromChannel(Integer.toString(channelid), Integer.toString(playlistid));
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (ResourceMissingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }
    @ApiOperation(value = "Add a playlist to a channel", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not Found!") })
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
            @ApiResponse(code = 404, message = "Not Found!") })
    public ResponseEntity<?> channelPlaylistSort(@PathVariable String channelid) {
        try {
            List<PlaylistChannel> channels = channelPlaylistService.playlistSort(channelid);
            return new ResponseEntity<>(channels, HttpStatus.OK);
        }
        catch (ChannelMissingException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        }
        catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @ApiOperation(value = "Change the position of a playlist in a channel", response = ResponseEntity.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success|OK"),
            @ApiResponse(code = 500, message = "Internal server error"),
            @ApiResponse(code = 404, message = "Not Found!") })
    @RequestMapping(path = "{channelid}/playlists/{playlistid}/to/{newIndex}", method = RequestMethod.PATCH)
    public ResponseEntity<?> positionUpdate(@PathVariable int channelid, @PathVariable int playlistid, @PathVariable int newIndex) {
        try {
            List<PlaylistChannel> channels = channelPlaylistService.playlistIndex(Integer.toString(channelid), Integer.toString(playlistid), newIndex);
            return new ResponseEntity<>(channels, HttpStatus.OK);
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
