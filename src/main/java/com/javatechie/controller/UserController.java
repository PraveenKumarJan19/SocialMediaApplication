package com.javatechie.controller;

import com.javatechie.dto.AuthRequest;
import com.javatechie.dto.LoginResponse;
import com.javatechie.entity.request.TokenRefreshRequest;
import com.javatechie.entity.request.User;
import com.javatechie.entity.response.TokenRefreshResponse;
import com.javatechie.entity.response.UserResponse;
import com.javatechie.service.JwtService;
import com.javatechie.service.UserService;
import com.javatechie.utils.UserUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserUtils userUtils;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/add")
    public ResponseEntity<UserResponse> addUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        return userService.save(user, bindingResult);
    }

    @GetMapping("/getAllUsers")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getAllUser() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> getUserById(@PathVariable("id") int id) {
        return userService.findAll();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void deleteUserById(@PathVariable("id") int id) {
        userService.deleteById(id);
    }

    @GetMapping("/getUserNameSuggestion/{userName}")
    public List<String> suggestUserNames(@PathVariable("userName") String userName) {
        return userUtils.suggestUsernames(userName);
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<User> searchUsers(@RequestParam String username) {
        return userService.findUsersByUsernameLike(username);
    }

    @PostMapping("/new")
    public ResponseEntity<UserResponse> addNewUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        return userService.save(user, bindingResult);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<LoginResponse> authenticateAndGetToken(@Valid @RequestBody AuthRequest authRequest, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String error = UserUtils.bindingResultErrors(bindingResult);
            return new ResponseEntity<>(LoginResponse.builder()
                    .message(error)
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build(), HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            String accessToken = jwtService.generateToken(authRequest.getUsername());
            String refreshToken = jwtService.generateRefreshToken(authRequest.getUsername());
            return new ResponseEntity<>(LoginResponse.builder().accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .userName(authRequest.getUsername())
                    .message("Login SuccessFul")
                    .statusCode(HttpStatus.OK.value())
                    .build(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(LoginResponse.builder()
                    .message("Authentication Failed")
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .build(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody TokenRefreshRequest request) {
        String refreshToken = request.getRefreshToken();
        if (jwtService.validateRefreshToken(refreshToken)) {
            String username = jwtService.extractUsername(refreshToken);
            String newAccessToken = jwtService.generateToken(username);
            return ResponseEntity.ok(new TokenRefreshResponse(newAccessToken));
        } else {
            return ResponseEntity.status(403).body("Invalid refresh token");
        }
    }
}
