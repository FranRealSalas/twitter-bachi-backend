package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.Tweet;

import java.util.List;
import java.util.Optional;

public interface TweetService {
    List<Tweet> findAll();

    Optional<Tweet> findById(Long id);

    Tweet save(Tweet tweet);

    void deleteById(Long id);
}
