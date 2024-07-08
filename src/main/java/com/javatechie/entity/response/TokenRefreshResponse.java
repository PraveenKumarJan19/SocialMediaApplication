package com.javatechie.entity.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
// Response Model
public class TokenRefreshResponse {
    private String accessToken;
}