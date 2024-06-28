package com.javatechie.repository;

import com.javatechie.entity.request.Post;
import com.javatechie.entity.request.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByUserInOrderByTimestampDesc(List<User> users);

}
