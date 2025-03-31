package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.ChatCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Chat;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.ChatRepository;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ChatServiceImp implements ChatService {
    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional(readOnly = true)
    @Override
    public List<Chat> findAllChats(Long id) {
        return this.chatRepository.findAllChatsOrderByIdAsc(id, 6);
    }

    @Override
    public Chat createChat(ChatCreationRequestDTO chatCreationRequestDTO) {
        Set<User> users = new HashSet<>(userRepository.findAllById(chatCreationRequestDTO.getUsersId()));
        User loggedUser = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();

        // Verificar si todos los usuarios existen
        if (users.size() != chatCreationRequestDTO.getUsersId().size()) {
            throw new IllegalArgumentException("Uno o más usuarios no existen.");
        }

        Chat chat = new Chat();
        users.add(loggedUser);
        chat.setUsers(users.stream().toList());

        return chatRepository.save(chat);
    }
}
