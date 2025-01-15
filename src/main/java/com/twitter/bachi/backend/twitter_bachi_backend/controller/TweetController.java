package com.twitter.bachi.backend.twitter_bachi_backend.controller;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.TweetCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.TweetEditRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.TweetResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tweets")
public class TweetController {

    @Autowired
    private TweetService tweetService;

    @GetMapping
    public List<TweetResponseDTO> listTweets() {
        return tweetService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showTweet(@PathVariable Long id) {
        Optional<TweetResponseDTO> tweetOptional = tweetService.findById(id);
        if (tweetOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(tweetOptional.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No se encontro el tweet por el id" + id));
    }

    @PostMapping
    public ResponseEntity<TweetResponseDTO> createTweet(@RequestBody TweetCreationRequestDTO tweet) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tweetService.save(tweet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TweetResponseDTO> editTweet(@RequestBody TweetEditRequestDTO tweet, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.edit(tweet, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTweet(@PathVariable Long id) {
        Optional<TweetResponseDTO> tweetOptional = tweetService.findById(id);
        if (tweetOptional.isPresent()) {
            tweetService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<List<TweetResponseDTO>> findCommentsByParentId(@PathVariable("id") Long parentId){
        return ResponseEntity.ok(tweetService.findCommentsByParentId(parentId));
    }
}
