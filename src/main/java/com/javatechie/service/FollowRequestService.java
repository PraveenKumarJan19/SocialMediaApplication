package com.javatechie.service;

import com.javatechie.entity.request.Follow;
import com.javatechie.entity.request.FollowRequest;
import com.javatechie.entity.request.User;
import com.javatechie.repository.FollowRepository;
import com.javatechie.repository.FollowRequestRepository;
import com.javatechie.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FollowRequestService {

    @Autowired
    private FollowRequestRepository followRequestRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private UserRepository userRepository;

    public void sendFollowRequest(String fromUsername, String toUsername) {
        User fromUser = userRepository.findByActualUserName(fromUsername).orElseThrow();
        User toUser = userRepository.findByActualUserName(toUsername).orElseThrow();

        if (fromUser == null || toUser == null) {
            throw new RuntimeException("User not found");
        }

        FollowRequest existingRequest = followRequestRepository.findByFromUserAndToUser(fromUser, toUser);
        if (existingRequest != null && "PENDING".equals(existingRequest.getStatus())) {
            throw new RuntimeException("Follow request already sent");
        }

        FollowRequest followRequest = new FollowRequest();
        followRequest.setFromUser(fromUser);
        followRequest.setToUser(toUser);
        followRequest.setStatus("PENDING");

        followRequestRepository.save(followRequest);
    }

    public void acceptFollowRequest(String toUsername, String fromUsername) {
        User toUser = userRepository.findByActualUserName(toUsername).orElseThrow();
        User fromUser = userRepository.findByActualUserName(fromUsername).orElseThrow();

        FollowRequest followRequest = followRequestRepository.findByFromUserAndToUser(fromUser, toUser);
        if (followRequest == null || !"PENDING".equals(followRequest.getStatus())) {
            throw new RuntimeException("No pending follow request found");
        }

        followRequest.setStatus("ACCEPTED");
        followRequestRepository.save(followRequest);

        Follow follow = new Follow();
        follow.setFollowed(toUser);
        follow.setFollower(fromUser);

        followRepository.save(follow);
    }

    public void rejectFollowRequest(String toUsername, String fromUsername) {
        User toUser = userRepository.findByUsername(toUsername);
        User fromUser = userRepository.findByUsername(fromUsername);

        FollowRequest followRequest = followRequestRepository.findByFromUserAndToUser(fromUser, toUser);
        if (followRequest == null || !"PENDING".equals(followRequest.getStatus())) {
            throw new RuntimeException("No pending follow request found");
        }

        followRequest.setStatus("REJECTED");
        followRequestRepository.save(followRequest);
    }

    public List<FollowRequest> getPendingFollowRequests(String toUsername) {
        User toUser = userRepository.findByUsername(toUsername);
        return followRequestRepository.findByToUserAndStatus(toUser, "PENDING");
    }

    public List<Follow> getFollowers(String username) {
        User user = userRepository.findByActualUserName(username).orElseThrow();
        return followRepository.findByFollowed(user);
    }

    public List<Follow> getFollowing(String username) {
        User user = userRepository.findByActualUserName(username).orElseThrow();
        return followRepository.findByFollower(user);
    }
}
