package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.ChatCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Chat;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ChatService {

    @Transactional(readOnly = true)
    List<Chat> findAllChats(Long id);

    Chat createChat(ChatCreationRequestDTO chatCreationRequestDTO);

}
