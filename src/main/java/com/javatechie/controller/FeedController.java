package com.javatechie.controller;

import com.javatechie.entity.request.Post;
import com.javatechie.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/feeds")
public class FeedController {

    @Autowired
    private FeedService feedService;

    @GetMapping("/{username}")
    public List<Post> getFeedForUser(@PathVariable String username) {
        return feedService.getFeedForUser(username);
    }
}
