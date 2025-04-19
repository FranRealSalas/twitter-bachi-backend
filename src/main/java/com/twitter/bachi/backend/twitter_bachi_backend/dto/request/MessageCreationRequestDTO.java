package com.twitter.bachi.backend.twitter_bachi_backend.dto.request;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.UserResponseDTO;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MessageCreationRequestDTO {

    @NotBlank
    private String content;

    private UserResponseDTO sender;

    private Long chatId;

    private ChatCreationRequestDTO chatCreationRequestDTO;
}
