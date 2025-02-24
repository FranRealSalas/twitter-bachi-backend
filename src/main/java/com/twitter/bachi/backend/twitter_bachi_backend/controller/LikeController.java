package com.twitter.bachi.backend.twitter_bachi_backend.controller;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.TweetResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/likes")
public class LikeController {

    @Autowired
    private TweetService service;

    @GetMapping("/{username}")
    public List<TweetResponseDTO> getTweetsByUser(@PathVariable String username) {
        return service.getTweetsLikedByUsername(username);
    }

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
