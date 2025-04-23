package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.google.gson.Gson;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.mapper.MessageMapper;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.mapper.UserMapper;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.MessageCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.MessageResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.UserResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Chat;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Message;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.ChatRepository;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.MessageRepository;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MessageServiceImp implements MessageService{
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private ChatService chatService;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Override
    @Transactional(readOnly = true)
    public List<MessageResponseDTO> findAllMessagesByChatId(Long chatId, Long id) {
        return messageRepository.findAllMessagesByChatId(chatId, 7, id).stream().map(message -> messageMapper.toDto(message)).toList();
    }

    @Override
    public MessageResponseDTO createMessage(MessageCreationRequestDTO messageCreationRequestDTO){
        Message message = new Message();
        message.setContent(messageCreationRequestDTO.getContent());

        Long chatId = messageCreationRequestDTO.getChatId();
        if (chatId != null) {
            Optional <Chat> chatOptional = chatRepository.findById(chatId);
            if(chatOptional.isPresent()){
                Chat chatDB = chatOptional.get();
                message.setChat(chatDB);
            }
        }
        else{
            List<Long> usersId = messageCreationRequestDTO.getChatCreationRequestDTO().getUsersId();
            User sender = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();

            Optional<Chat> existingChat = chatRepository.findChatByExactParticipants(usersId, usersId.size());

            Chat chatDB;
            if (existingChat.isPresent()) {
                chatDB = existingChat.get();
            } else {
                chatDB = chatService.createChatDB(messageCreationRequestDTO.getChatCreationRequestDTO());
            }
            message.setChat(chatDB);
        }
        message.getChat().setDate(new Date());
        chatRepository.save(message.getChat());

        User user = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();
        message.setSender(user);

        // Guardar el mensaje en la base de datos
        Message savedMessage = messageRepository.save(message);

        // Mapear la entidad Message a MessageResponseDTO
        MessageResponseDTO messageResponseDTO = messageMapper.toDto(savedMessage);

        // Ahora mapeamos el User a un UserResponseDTO y lo asignamos al mensaje
        UserResponseDTO userResponseDTO = userMapper.toDto(user);
        messageResponseDTO.setSender(userResponseDTO);

        sendMessageToWebSocket(messageResponseDTO, savedMessage.getChat().getId());

        return messageResponseDTO;

    }

    public void sendMessageToWebSocket(MessageResponseDTO messageResponseDTO, Long chatId){
        simpMessagingTemplate.convertAndSend(
                "/topic/messages/"+ chatId,
                new Gson().toJson(messageResponseDTO));
    }
}
