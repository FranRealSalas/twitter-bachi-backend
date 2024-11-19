package com.twitter.bachi.backend.twitter_bachi_backend.dto;

import lombok.Data;

@Data
public class LoginRequestDTO {

    private String username;
    private String password;
}
