package com.twitter.bachi.backend.twitter_bachi_backend.repository;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
