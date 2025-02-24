package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.UserNotification;

import java.util.List;

public interface UserNotificationService {

    List<UserNotification> findByUser_username(String username);
}
