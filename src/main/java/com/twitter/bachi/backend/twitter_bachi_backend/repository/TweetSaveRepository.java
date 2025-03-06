package com.twitter.bachi.backend.twitter_bachi_backend.repository;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.Tweet;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.TweetSave;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TweetSaveRepository extends JpaRepository<TweetSave, Long> {
    Optional<TweetSave> findByUserAndTweet(User user, Tweet tweet);

    boolean existsByUserAndTweet(User user, Tweet tweet);

    long countByTweet(Tweet tweet);

    @Query(value = "SELECT t FROM TweetSave t WHERE t.user.username = :username AND (:id IS NULL OR t.tweet.id < :id) ORDER BY t.tweet.id DESC LIMIT :limit")
    List<TweetSave> findByUser_username(String username, Integer limit, Long id);
}
