package com.twitter.bachi.backend.twitter_bachi_backend.repository;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.UserNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserNotificationRepository extends JpaRepository <UserNotification, Long> {

    List<UserNotification> findByUser_username(String username);
}
