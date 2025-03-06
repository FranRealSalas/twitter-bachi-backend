package com.twitter.bachi.backend.twitter_bachi_backend.repository;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.Tweet;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TweetRepository extends JpaRepository<Tweet, Long> {

    @Query("SELECT t FROM Tweet t WHERE t.parentTweet IS NULL AND (:id IS NULL OR t.id < :id) ORDER BY t.id DESC LIMIT :limit")
    List<Tweet> findAllByParentTweetIsNullOrderByIdDesc(Long id, Integer limit);

    List<Tweet> findByParentTweet_Id(Long parentId);

    @Query(value = "SELECT t FROM Tweet t WHERE t.parentTweet IS NULL AND t.user.username = :username AND (:id IS NULL OR t.id < :id) ORDER BY t.id DESC LIMIT :limit")
    List<Tweet> findByUser_usernameOrderByIdDesc(String username, Integer limit, Long id);

    @Query(value = "SELECT t FROM Tweet t WHERE t.parentTweet IS NOT NULL AND t.user.username = :username AND (:id IS NULL OR t.id < :id) ORDER BY t.id DESC LIMIT :limit")
    List<Tweet> findByUser_usernameAndParentTweetNotNull(String username, Integer limit, Long id);

    @Query(value = "SELECT t FROM Tweet t WHERE SIZE(t.images) > 0 AND t.user.username = :username AND (:id IS NULL OR t.id < :id) ORDER BY t.id DESC LIMIT :limit")
    List<Tweet> findByUser_usernameAndImagesNotNull(String username, Integer limit, Long id);

    int countByParentTweet_Id(Long parentId);

    Long countByUser(User user);

    @Query("SELECT t FROM Tweet t WHERE t.user IN :list")
    List<Tweet> findByFolloweds(List<User> list);
}
