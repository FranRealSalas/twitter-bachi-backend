package com.twitter.bachi.backend.twitter_bachi_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class UserFollow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User followed;

    @ManyToOne
    private User follower;
}
