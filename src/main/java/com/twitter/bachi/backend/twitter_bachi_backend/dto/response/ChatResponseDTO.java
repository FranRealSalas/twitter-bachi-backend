package com.twitter.bachi.backend.twitter_bachi_backend.dto.response;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatResponseDTO {

    private Long id;

    private List<UserResponseDTO> users;

    private MessageResponseDTO lastMessage;

}
