package com.twitter.bachi.backend.twitter_bachi_backend.controller;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.TweetResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/saves")
public class SaveController {

    @Autowired
    private TweetService service;

    @GetMapping("/{username}")
    public List<TweetResponseDTO> getTweetsByUser(@PathVariable String username, @RequestParam(required = false) Long id) {
        return service.getTweetsSavedByUsername(username,id);
    }

    @PostMapping("/give")
    public ResponseEntity<?> giveSave(@RequestParam Long tweetId) {
        service.giveSave(tweetId);
        return ResponseEntity.ok("Save successfully");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeSave(@RequestParam Long tweetId) {
        service.removeSave(tweetId);
        return ResponseEntity.ok("Save removed");
    }
}