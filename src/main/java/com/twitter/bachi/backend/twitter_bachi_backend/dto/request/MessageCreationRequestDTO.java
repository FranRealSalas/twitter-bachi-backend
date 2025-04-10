package com.twitter.bachi.backend.twitter_bachi_backend.dto.request;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.UserResponseDTO;
import lombok.Data;

@Data
public class MessageCreationRequestDTO {

    private String content;

    private UserResponseDTO sender;

    private Long chatId;

    private ChatCreationRequestDTO chatCreationRequestDTO;
}
