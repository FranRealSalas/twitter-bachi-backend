package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.MessageCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Message;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.MessageRepository;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MessageServiceImp implements MessageService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Message> findAllMessagesByChatId(Long chatId, Long id) {
        return messageRepository.findAllMessagesByChatId(chatId, 7, id);
    }

    @Override
    public Message createMessage(MessageCreationRequestDTO messageCreationRequestDTO){
        Message message = new Message();
        message.setContent(messageCreationRequestDTO.getContent());
        message.setChat(messageCreationRequestDTO.getChat());

        User loggedUser = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();
        message.setSender(loggedUser);

        return messageRepository.save(message);
    }
}
