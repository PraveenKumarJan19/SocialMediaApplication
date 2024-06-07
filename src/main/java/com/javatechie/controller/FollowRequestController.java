package com.javatechie.controller;

import com.javatechie.entity.FollowRequest;
import com.javatechie.service.FollowRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow-requests")
public class FollowRequestController {

    @Autowired
    private FollowRequestService followRequestService;

    @PostMapping("/send")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void sendFollowRequest(@RequestParam String fromUsername, @RequestParam String toUsername) {
        followRequestService.sendFollowRequest(fromUsername, toUsername);
    }

    @PostMapping("/accept")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void acceptFollowRequest(@RequestParam String toUsername, @RequestParam String fromUsername) {
        followRequestService.acceptFollowRequest(toUsername, fromUsername);
    }

    @PostMapping("/reject")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void rejectFollowRequest(@RequestParam String toUsername, @RequestParam String fromUsername) {
        followRequestService.rejectFollowRequest(toUsername, fromUsername);
    }

    @GetMapping("/pending")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<FollowRequest> getPendingFollowRequests(@RequestParam String toUsername) {
        return followRequestService.getPendingFollowRequests(toUsername);
    }
}
