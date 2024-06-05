package com.javatechie.controller;

import com.javatechie.entity.Post;
import com.javatechie.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @RequestMapping("/addPost")
    public void addPost(Post post) {
        postService.save(post);
    }

    @GetMapping("/getAllPosts")
    public List<Post> getAllPosts() {
        return postService.findAll();
    }

    @GetMapping("/{id}")
    public Post getPostById(int id) {
        return postService.findById(id).orElseThrow();
    }

    @DeleteMapping("delete/{id}")
    public void deletePostById(int id) {
        postService.deleteById(id);
    }
}
