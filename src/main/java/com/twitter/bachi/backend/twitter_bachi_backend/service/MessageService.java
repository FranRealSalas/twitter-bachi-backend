package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.MessageCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.MessageResponseDTO;

import java.util.List;

public interface MessageService {

    MessageResponseDTO createMessage(MessageCreationRequestDTO messageCreationRequestDTO);

    List<MessageResponseDTO> findAllMessagesByChatId(Long chatId, Long id);
}
