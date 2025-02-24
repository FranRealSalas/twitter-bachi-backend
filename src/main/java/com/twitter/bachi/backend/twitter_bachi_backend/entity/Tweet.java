package com.twitter.bachi.backend.twitter_bachi_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data

public class Tweet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String content;

    @ManyToOne
    private User user;

    @ManyToOne
    private Tweet parentTweet;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date date;
}
