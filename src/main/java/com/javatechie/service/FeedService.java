package com.javatechie.service;

import com.javatechie.entity.request.Follow;
import com.javatechie.entity.request.Post;
import com.javatechie.entity.request.User;
import com.javatechie.repository.FollowRepository;
import com.javatechie.repository.PostRepository;
import com.javatechie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FeedService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FollowRepository followRepository;

    public List<Post> getFeedForUser(String username) {
        User user = userRepository.findByUsername(username);
        List<User> followingUsers = followRepository.findByFollower(user)
                .stream()
                .map(Follow::getFollowed)
                .collect(Collectors.toList());

        return postRepository.findByUserInOrderByTimestampDesc(followingUsers);
    }
}
