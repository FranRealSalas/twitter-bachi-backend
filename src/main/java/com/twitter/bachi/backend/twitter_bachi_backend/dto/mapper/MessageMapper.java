package com.twitter.bachi.backend.twitter_bachi_backend.dto.mapper;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.MessageResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Message;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {MessageMapper.class})
public abstract class MessageMapper {

    public abstract MessageResponseDTO toDto(Message message);

}
