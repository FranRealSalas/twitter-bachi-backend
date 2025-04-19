package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.ChatCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.ChatResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Chat;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ChatService {

    @Transactional(readOnly = true)
    Page<ChatResponseDTO> findAllChats(Integer page);

    ChatResponseDTO createChat(ChatCreationRequestDTO chatCreationRequestDTO);

    Chat createChatDB(ChatCreationRequestDTO chatCreationRequestDTO);

    ChatResponseDTO findChatByExactParticipants(List<Long> userIds);
}
