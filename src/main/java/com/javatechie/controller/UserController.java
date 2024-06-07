package com.javatechie.controller;

import com.javatechie.entity.User;
import com.javatechie.service.UserService;
import com.javatechie.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserUtils userUtils;

    @PostMapping("/add")
    public String addUser(@RequestBody User user) {
        userService.save(user);
        return "User Added Succesfully";
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
}
