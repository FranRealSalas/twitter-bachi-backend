package com.twitter.bachi.backend.twitter_bachi_backend.controller;

import com.twitter.bachi.backend.twitter_bachi_backend.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private TweetService service;

    @PostMapping("/give")
    public ResponseEntity<?> giveLike(@RequestParam Long tweetId) {
        service.giveLike(tweetId);
        return ResponseEntity.ok("Like successfully");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeLike(@RequestParam Long tweetId) {
        service.removeLike(tweetId);
        return ResponseEntity.ok("Like removed");
    }
}
