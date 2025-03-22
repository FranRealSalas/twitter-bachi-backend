package com.twitter.bachi.backend.twitter_bachi_backend.dto.request;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.Chat;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import lombok.Data;



@Data
public class MessageCreationRequestDTO {

    private String content;

    private User sender;

    private Chat chat;
}
