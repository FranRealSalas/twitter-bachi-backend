package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.ChatCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Chat;

import java.util.List;

public interface ChatService {

    List<Chat> findAllChats();

    Chat createChat(ChatCreationRequestDTO chatCreationRequestDTO);


}
