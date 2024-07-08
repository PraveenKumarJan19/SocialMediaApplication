package com.javatechie.repository;

import com.javatechie.entity.request.Follow;
import com.javatechie.entity.request.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByFollower(User follower);

    List<Follow> findByFollowed(User followed);
}
