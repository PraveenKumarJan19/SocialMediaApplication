package com.javatechie.service;

import com.javatechie.entity.FollowRequest;
import com.javatechie.entity.User;
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
    private UserRepository userRepository;

    public void sendFollowRequest(String fromUsername, String toUsername) {
        User fromUser = userRepository.findByUsername(fromUsername);
        User toUser = userRepository.findByUsername(toUsername);

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
        User toUser = userRepository.findByUsername(toUsername);
        User fromUser = userRepository.findByUsername(fromUsername);

        FollowRequest followRequest = followRequestRepository.findByFromUserAndToUser(fromUser, toUser);
        if (followRequest == null || !"PENDING".equals(followRequest.getStatus())) {
            throw new RuntimeException("No pending follow request found");
        }

        followRequest.setStatus("ACCEPTED");
        followRequestRepository.save(followRequest);

        // Optionally, add to UserRelationship or other actions
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
}
