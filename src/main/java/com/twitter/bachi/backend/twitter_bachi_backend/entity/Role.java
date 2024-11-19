package com.twitter.bachi.backend.twitter_bachi_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="roles")

public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}