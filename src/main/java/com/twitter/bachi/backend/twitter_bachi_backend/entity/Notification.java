package com.twitter.bachi.backend.twitter_bachi_backend.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;

@Entity
@Data
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String icon;

    private String iconType;

    private String href;

    private String profileImage;

    private String title;

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date date;
}
