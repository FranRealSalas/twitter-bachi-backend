package com.twitter.bachi.backend.twitter_bachi_backend.dto.request;

import lombok.Data;

@Data
public class TweetCreationRequestDTO {

    private String content;

    private Long parentTweetId;
}
