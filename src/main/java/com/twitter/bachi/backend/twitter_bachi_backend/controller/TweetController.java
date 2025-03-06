package com.twitter.bachi.backend.twitter_bachi_backend.controller;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.TweetCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.TweetEditRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.TweetResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.service.TweetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tweets")
public class TweetController {

    @Autowired
    private TweetService tweetService;

    @GetMapping
    public List<TweetResponseDTO> listTweets(@RequestParam(required = false) Long id) {
        return tweetService.findAll(id);
    }

    @GetMapping("/by-username/{username}")
    public List<TweetResponseDTO> listTweetsByUsername(@PathVariable String username, @RequestParam(required = false) Long id) {
        return tweetService.getTweetsByUsername(username, id);
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
    public ResponseEntity<TweetResponseDTO> createTweet(@RequestPart("content") TweetCreationRequestDTO tweet, @RequestPart(required = false) MultipartFile[] images) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tweetService.save(tweet, images));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TweetResponseDTO> editTweet(@RequestBody TweetEditRequestDTO tweet, @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(tweetService.edit(tweet, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTweet(@PathVariable Long id) {
        Optional<TweetResponseDTO> tweetOptional = tweetService.findById(id);
        if (tweetOptional.isPresent()) {
            if (tweetOptional.get().getUser().getUsername().equals((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
                tweetService.deleteById(id);
                return ResponseEntity.noContent().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<List<TweetResponseDTO>> findCommentsByParentId(@PathVariable("id") Long parentId) {
        return ResponseEntity.ok(tweetService.findCommentsByParentId(parentId));
    }

    @GetMapping("comments/by-username/{username}")
    public List<TweetResponseDTO> listCommentsByUsername(@PathVariable String username, @RequestParam(required = false) Long id) {
        return tweetService.getCommentsByUsername(username, id);
    }

    @GetMapping("/with-image/{username}")
    public List<TweetResponseDTO> listTweetsWithImagesByUsername(@PathVariable String username, @RequestParam(required = false) Long id) {
        return tweetService.getTweetsWithImagesByUsername(username, id);
    }

    @GetMapping("/uploads/tweetImages/{photoName}")
    public ResponseEntity<Resource> showTweetPhotos(@PathVariable String photoName) {
        Path fileRoute = Paths.get("uploads/tweetImages").resolve(photoName).toAbsolutePath();
        Resource resource = null;

        try {
            resource = new UrlResource(fileRoute.toUri());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpHeaders header = new HttpHeaders();
        assert resource != null;

        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    }

    @GetMapping("/by-followed")
    public List<TweetResponseDTO> getTweetsByFolloweds() {
        return tweetService.getTweetsByFolloweds();
    }
}
