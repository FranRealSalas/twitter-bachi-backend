package com.twitter.bachi.backend.twitter_bachi_backend.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class ChatCreationRequestDTO {
    private List<Long> usersId;
}
