package com.javatechie.controller;

import com.javatechie.entity.request.Follow;
import com.javatechie.entity.request.FollowRequest;
import com.javatechie.service.FollowRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/follow-requests")
public class FollowRequestController {

    @Autowired
    private FollowRequestService followRequestService;

    @GetMapping("/hello")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getHello() {
        return "Hello";
    }

    @GetMapping("/send")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> sendFollowRequest(@RequestParam String fromUsername, @RequestParam String toUsername) {
        if (toUsername == null || fromUsername == null) {
            return new ResponseEntity<>("Missing required parameters", HttpStatus.BAD_REQUEST);
        }
        try {
            followRequestService.sendFollowRequest(fromUsername, toUsername);
            return new ResponseEntity<>("Follow request sent", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/accept")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> acceptFollowRequest(@RequestParam String toUsername, @RequestParam String fromUsername) {
        if (toUsername == null || fromUsername == null) {
            return new ResponseEntity<>("Missing required parameters", HttpStatus.BAD_REQUEST);
        }

        try {
            followRequestService.acceptFollowRequest(toUsername, fromUsername);
            return new ResponseEntity<>("Follow request accepted", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/reject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> rejectFollowRequest(@RequestParam String toUsername, @RequestParam String fromUsername) {
        if (toUsername == null || fromUsername == null) {
            return new ResponseEntity<>("Missing required parameters", HttpStatus.BAD_REQUEST);
        }
        try {
            followRequestService.rejectFollowRequest(toUsername, fromUsername);
            return new ResponseEntity<>("Follow request rejected", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/pending")
    public ResponseEntity<List<FollowRequest>> getPendingFollowRequests(
            @RequestParam(required = false) String toUsername) {
        if (toUsername == null) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        try {
            List<FollowRequest> pendingRequests = followRequestService.getPendingFollowRequests(toUsername);
            return new ResponseEntity<>(pendingRequests, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/followers/{username}")
    public List<Follow> getFollowers(@PathVariable String username) {
        return followRequestService.getFollowers(username);
    }

    @GetMapping("/following/{username}")
    public List<Follow> getFollowing(@PathVariable String username) {
        return followRequestService.getFollowing(username);
    }
}
