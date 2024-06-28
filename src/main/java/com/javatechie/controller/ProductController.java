package com.javatechie.controller;

import com.javatechie.dto.AuthRequest;
import com.javatechie.dto.LoginResponse;
import com.javatechie.entity.request.Product;
import com.javatechie.entity.request.UserInfo;
import com.javatechie.entity.response.UserResponse;
import com.javatechie.service.JwtService;
import com.javatechie.service.ProductService;
import com.javatechie.utils.UserUtils;
import jakarta.validation.Valid;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.naming.Binding;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService service;
    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/new")
    public ResponseEntity<UserResponse> addNewUser(@Valid @RequestBody UserInfo userInfo, BindingResult bindingResult) {
        return service.addUser(userInfo, bindingResult);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Product> getAllTheProducts() {
        return service.getProducts();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public Product getProductById(@PathVariable int id) {
        return service.getProduct(id);
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
            return new ResponseEntity<>(LoginResponse.builder().accessToken(accessToken)
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
}
