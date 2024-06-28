package com.javatechie.controller;

import com.javatechie.entity.request.Post;
import com.javatechie.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @RequestMapping("/addPost")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String addPost(@RequestBody Post post) {
        postService.save(post);
        return "Post added successfully";
    }

    @GetMapping("/getAllPosts")
    public List<Post> getAllPosts() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public Post getPostById(@PathVariable int id) {
        return postService.findById(id).orElseThrow();
    }

    @DeleteMapping("delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deletePostById(@PathVariable int id) {
        postService.deleteById(id);
        return "Post deleted successfully";
    }
}
