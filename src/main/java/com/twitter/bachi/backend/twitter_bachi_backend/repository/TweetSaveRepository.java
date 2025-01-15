package com.twitter.bachi.backend.twitter_bachi_backend.repository;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.Tweet;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.TweetSave;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetSaveRepository extends JpaRepository<TweetSave, Long> {
    Optional<TweetSave> findByUserAndTweet(User user, Tweet tweet);

    boolean existsByUserAndTweet(User user, Tweet tweet);

    long countByTweet(Tweet tweet);

    List<TweetSave> findByUser_username(String username);
}
