package com.twitter.bachi.backend.twitter_bachi_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.UserResponseDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne
    private User sender;

    @ManyToOne
    @JsonIgnore
    private Chat chat;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date date;
}
