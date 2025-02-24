package com.twitter.bachi.backend.twitter_bachi_backend.repository;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.TweetResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Tweet;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {
    List<Tweet> findAllByParentTweetIsNull();

    List<Tweet> findByParentTweet_Id(Long parentId);

    List<Tweet> findByUser_username(String username);

    List<Tweet> findByUser_usernameAndParentTweetNotNull(String username);

    List<Tweet> findByUser_usernameAndImagesNotNull(String username);

    int countByParentTweet_Id(Long parentId);

    @Query("SELECT t FROM Tweet t WHERE t.user IN :list")
    List<Tweet> findByFolloweds(List<User> list);
}
