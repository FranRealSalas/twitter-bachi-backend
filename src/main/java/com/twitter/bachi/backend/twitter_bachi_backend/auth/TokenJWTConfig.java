package com.twitter.bachi.backend.twitter_bachi_backend.auth;

import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;

public class TokenJWTConfig {

    public static final String CONTENT_TYPE = "application/json";
    public static final String PREFIX_TOKEN = "Bearer ";
    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String SECRET_KEY = "W10iLCJ1c2VybmFtZSI6IkFkbWluIiwiaWF0IjoxNzI5NjQ2OTQ2LCJle";
}
