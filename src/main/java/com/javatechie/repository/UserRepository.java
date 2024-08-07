package com.javatechie.repository;

import com.javatechie.entity.request.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    User findByUsername(String username);

    @Query("SELECT u FROM User u WHERE u.actualUserName = :actualUserName")
    Optional<User> findByActualUserName(@Param("actualUserName") String actualUserName);

    List<User> findByUsernameContainingIgnoreCase(String username);
}
