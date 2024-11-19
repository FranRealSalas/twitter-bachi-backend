package com.twitter.bachi.backend.twitter_bachi_backend.dto;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private String token;
    private String username;
    private String message;
}
