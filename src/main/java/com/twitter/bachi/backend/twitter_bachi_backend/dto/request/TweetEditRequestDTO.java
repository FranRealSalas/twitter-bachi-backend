package com.twitter.bachi.backend.twitter_bachi_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class TweetEditRequestDTO {

    @NotBlank
    private String content;
}
