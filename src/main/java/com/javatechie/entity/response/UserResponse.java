package com.javatechie.entity.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserResponse {
    // Getters and setters
    private String message;
    private int statusCode;
}
