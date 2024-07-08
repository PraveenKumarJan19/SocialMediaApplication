package com.javatechie.entity.request;

import lombok.*;

// Request Model
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TokenRefreshRequest {
    private String refreshToken;
}