package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.google.gson.Gson;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.mapper.ChatMapper;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.ChatCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.ChatResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.MessageResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Chat;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.ChatRepository;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class ChatServiceImp implements ChatService {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  ChatMapper chatMapper;

    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Transactional(readOnly = true)
    @Override
    public Page<ChatResponseDTO> findAllChats(Integer page) {
        User loggedUser = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();
        return this.chatRepository.findAllByUsersContainingOrderByDateDesc(loggedUser, PageRequest.of(page,5)).map(chat -> chatMapper.toDto(chat));
    }

    @Override
    public ChatResponseDTO findChatByExactParticipants(List<Long> userIds) {
        // Intentamos obtener el chat de la base de datos
        Optional<Chat> chatOptional = chatRepository.findChatByExactParticipants(userIds, userIds.size());

        // Si el chat está presente, mapeamos a ChatResponseDTO, si no, lanzamos una excepción
        return chatOptional
                .map(chat -> chatMapper.toDto(chat)) // Si está presente, mapeamos a DTO
                .orElseThrow(() -> new EntityNotFoundException("No chat found with the exact participants.")); // Si no, lanzamos excepción
    }


    @Override
    public ChatResponseDTO createChat(ChatCreationRequestDTO chatCreationRequestDTO) {
        return chatMapper.toDto(createChatDB(chatCreationRequestDTO));
    }

    @Override
    public Chat createChatDB(ChatCreationRequestDTO chatCreationRequestDTO){
        Set<User> users = new HashSet<>(userRepository.findAllById(chatCreationRequestDTO.getUsersId()));
        User loggedUser = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();

        // Verificar si todos los usuarios existen
        if (users.size() != chatCreationRequestDTO.getUsersId().size()) {
            throw new IllegalArgumentException("Uno o más usuarios no existen.");
        }

        Chat chat = new Chat();
        users.add(loggedUser);
        chat.setUsers(new ArrayList<>(users));

        return chatRepository.save(chat);
    }
}
