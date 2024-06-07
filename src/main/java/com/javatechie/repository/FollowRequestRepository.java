package com.javatechie.repository;

import com.javatechie.entity.FollowRequest;
import com.javatechie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FollowRequestRepository extends JpaRepository<FollowRequest, Long> {
    List<FollowRequest> findByToUserAndStatus(User toUser, String status);

    FollowRequest findByFromUserAndToUser(User fromUser, User toUser);
}
