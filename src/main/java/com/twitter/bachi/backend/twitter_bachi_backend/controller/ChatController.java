package com.twitter.bachi.backend.twitter_bachi_backend.controller;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.ChatCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.ChatResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Chat;
import com.twitter.bachi.backend.twitter_bachi_backend.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping
    public Page<ChatResponseDTO> findAllChats(@RequestParam Integer page){
        return chatService.findAllChats(page);
    }

    @GetMapping("/by-participants")
    public ChatResponseDTO findChatByExactParticipants(@RequestParam List<Long>usersIds){
        return chatService.findChatByExactParticipants(usersIds);
    }

    @PostMapping
    public ResponseEntity<ChatResponseDTO> createChat(@RequestBody ChatCreationRequestDTO chatCreationRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(chatService.createChat(chatCreationRequestDTO));
    }
}
