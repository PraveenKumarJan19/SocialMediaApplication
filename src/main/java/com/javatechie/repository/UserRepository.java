package com.javatechie.repository;

import com.javatechie.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByName(String username);

    Optional<User> findByEmail(String email);
}
