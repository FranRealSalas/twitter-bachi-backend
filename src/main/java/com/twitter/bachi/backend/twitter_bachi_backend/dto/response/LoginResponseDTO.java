package com.twitter.bachi.backend.twitter_bachi_backend.dto.response;

import lombok.Data;

@Data
public class LoginResponseDTO {

    private String token;
    private String username;
    private String message;
}
