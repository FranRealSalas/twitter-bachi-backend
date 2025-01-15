package com.twitter.bachi.backend.twitter_bachi_backend.controller;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.UserResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Autowired
    private UserService service;

    @PostMapping("/give")
    public ResponseEntity<?> giveFollow(@RequestParam String username) {
        service.giveFollow(username);
        return ResponseEntity.ok("Follow successfully");
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeFollow(@RequestParam String username) {
        service.removeFollow(username);
        return ResponseEntity.ok("Follow removed");
    }
}
