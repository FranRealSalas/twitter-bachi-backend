package com.twitter.bachi.backend.twitter_bachi_backend.controller;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.Tweet;
import com.twitter.bachi.backend.twitter_bachi_backend.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = {"http://localhost:3000"})
@RestController
@RequestMapping("/api/tweets")
public class TweetController {

    @Autowired
    private TweetService tweetService;

    @GetMapping
    public List<Tweet> listTweets(){
        return tweetService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> showTweet(@PathVariable Long id){
        Optional <Tweet> tweetOptional = tweetService.findById(id);
        if (tweetOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(tweetOptional.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "No se encontro el tweet por el id" + id));
    }

    @PostMapping
    public ResponseEntity<?> createTweet(@RequestBody Tweet tweet){
        return ResponseEntity.status(HttpStatus.CREATED).body(tweetService.save(tweet));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editTweet(@RequestBody Tweet tweet, @PathVariable Long id){
        Optional<Tweet> tweetOptional = tweetService.findById(id);
        if (tweetOptional.isPresent()){
            Tweet tweetDB = tweetOptional.get();
            tweetDB.setContent(tweet.getContent());

            return ResponseEntity.ok(tweetService.save(tweetDB));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTweet(@PathVariable Long id){
        Optional<Tweet> tweetOptional = tweetService.findById(id);
        if (tweetOptional.isPresent()){
            tweetService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
