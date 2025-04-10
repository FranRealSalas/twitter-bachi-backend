package com.twitter.bachi.backend.twitter_bachi_backend.dto.mapper;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.ChatResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.MessageResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Chat;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Message;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.MessageRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {ChatMapper.class})
public abstract class ChatMapper {

    @Autowired
    protected MessageRepository messageRepository;

    @Autowired
    protected MessageMapper messageMapper;

    @Mapping(target = "lastMessage", expression = "java(getLastMessage(chat.getId()))")
    public abstract ChatResponseDTO toDto(Chat chat);

    public MessageResponseDTO getLastMessage(Long chatId) {
        List<Message> lista = messageRepository.findAllMessagesByChatId( chatId ,1 ,null);
        if (lista.isEmpty()){
            return null;
        }
        return messageMapper.toDto(lista.get(0));
    }
}
