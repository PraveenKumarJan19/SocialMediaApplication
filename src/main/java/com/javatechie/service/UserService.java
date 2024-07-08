package com.javatechie.service;

import com.javatechie.entity.request.User;
import com.javatechie.entity.response.UserResponse;
import com.javatechie.repository.UserRepository;
import com.javatechie.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseEntity<UserResponse> save(User user, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String error = UserUtils.bindingResultErrors(bindingResult);
            var userRes = new UserResponse(error, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(userRes, HttpStatus.BAD_REQUEST);
        }

        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        } catch (Exception e) {
            var userRes = new UserResponse(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(userRes, HttpStatus.BAD_REQUEST);
        }
        var userRes = UserResponse.builder()
                .message("User Added successfully")
                .statusCode(HttpStatus.OK.value())
                .build();
        return new ResponseEntity<>(userRes, HttpStatus.OK);
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }

    public User update(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> findUsersByUsernameLike(String username) {
        return userRepository.findByUsernameContainingIgnoreCase(username);
    }
}
