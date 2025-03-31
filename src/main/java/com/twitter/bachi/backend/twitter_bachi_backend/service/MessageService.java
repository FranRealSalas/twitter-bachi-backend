package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.MessageCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Message;

import java.util.List;

public interface MessageService {
    Message createMessage(MessageCreationRequestDTO messageCreationRequestDTO);

    public List<Message> findAllMessagesByChatId(Long chatId, Long id);
}
