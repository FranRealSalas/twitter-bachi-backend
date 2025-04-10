package com.twitter.bachi.backend.twitter_bachi_backend.dto.response;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.Chat;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponseDTO {

    private Long id;

    private String content;

    private UserResponseDTO sender;

    private Date date;
}
