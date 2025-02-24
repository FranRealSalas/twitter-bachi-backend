package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.UserNotification;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.UserNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserNotificationServiceImp implements UserNotificationService {

    @Autowired
    UserNotificationRepository userNotificationRepository;

    @Override
    public List<UserNotification> findByUser_username(String username) {
        return userNotificationRepository.findByUser_username(username);
    }
}
