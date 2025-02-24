package com.twitter.bachi.backend.twitter_bachi_backend.controller;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.UserNotification;
import com.twitter.bachi.backend.twitter_bachi_backend.service.UserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    UserNotificationService userNotificationService;

    @GetMapping("/{username}")
    public List<UserNotification> notificationsByUsername(@PathVariable String username){
        return userNotificationService.findByUser_username(username);
    }
}
