package com.twitter.bachi.backend.twitter_bachi_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TweetResponseDTO {

    private Long id;

    private String content;

    private UserResponseDTO user;

    private Long parentTweetId;

    private Boolean liked;

    private Boolean saved;
}
