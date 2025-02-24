package com.twitter.bachi.backend.twitter_bachi_backend.dto.request;

import lombok.Data;

@Data
public class UserCreationRequestDTO {

    private String username;
    private String editableName;
}
