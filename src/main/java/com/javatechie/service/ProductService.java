package com.javatechie.service;

import com.javatechie.entity.request.Product;
import com.javatechie.entity.request.UserInfo;
import com.javatechie.entity.response.UserResponse;
import com.javatechie.repository.UserInfoRepository;
import com.javatechie.utils.UserUtils;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class ProductService {

    List<Product> productList = null;

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void loadProductsFromDB() {
        productList = IntStream.rangeClosed(1, 100)
                .mapToObj(i -> Product.builder()
                        .productId(i)
                        .name("product " + i)
                        .qty(new Random().nextInt(10))
                        .price(new Random().nextInt(5000)).build()
                ).collect(Collectors.toList());
    }


    public List<Product> getProducts() {
        return productList;
    }

    public Product getProduct(int id) {
        return productList.stream()
                .filter(product -> product.getProductId() == id)
                .findAny()
                .orElseThrow(() -> new RuntimeException("product " + id + " not found"));
    }

    public ResponseEntity<UserResponse> addUser(UserInfo userInfo, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            String error = UserUtils.bindingResultErrors(bindingResult);
            var userRes = new UserResponse(error, HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity<>(userRes, HttpStatus.BAD_REQUEST);
        }

        try {
            userInfo.setPassword(passwordEncoder.encode(userInfo.getPassword()));
            repository.save(userInfo);
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
}
