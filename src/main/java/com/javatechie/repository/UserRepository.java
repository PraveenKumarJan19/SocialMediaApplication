package com.javatechie.repository;

import com.javatechie.entity.request.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    User findByUsername(String username);

    List<User> findByUsernameContainingIgnoreCase(String username);
}
