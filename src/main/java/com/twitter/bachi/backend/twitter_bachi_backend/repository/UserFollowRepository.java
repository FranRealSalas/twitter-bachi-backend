package com.twitter.bachi.backend.twitter_bachi_backend.repository;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
    Optional<UserFollow> findByFollowerAndFollowed(User follower, User followed);

    boolean existsByFollowerAndFollowed (User follower, User followed);

    long countByFollowed(User followed);

    long countByFollower(User follower);

    List<UserFollow> findByFollowed_username(String followed);

    List<UserFollow> findByFollower_username(String follower);
}
