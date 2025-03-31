package com.twitter.bachi.backend.twitter_bachi_backend.controller;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.MessageCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Chat;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Message;
import com.twitter.bachi.backend.twitter_bachi_backend.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    @Autowired
    MessageService messageService;

    @PostMapping
    public ResponseEntity<Message> createMessage(@RequestBody  MessageCreationRequestDTO messageCreationRequestDTO){
        return ResponseEntity.status(HttpStatus.CREATED).body(messageService.createMessage(messageCreationRequestDTO));
    }

    @GetMapping("/{chatId}")
    public List<Message> findAllMessagesByChatId(@PathVariable Long chatId, @RequestParam(required = false) Long id){
        return messageService.findAllMessagesByChatId(chatId, id);
    }
}
