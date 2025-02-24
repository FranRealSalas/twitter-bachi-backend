package com.twitter.bachi.backend.twitter_bachi_backend.controller;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.UserCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.UserEditRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.UserResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.UserFollow;
import com.twitter.bachi.backend.twitter_bachi_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    public List<UserResponseDTO> list(){
        return userService.findAll();
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        Optional<UserResponseDTO> userOptional = userService.findByUsername(username);
        if (userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(userOptional.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "El Usuario no se encontro por el username" + username));
    }

     @GetMapping("/")

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody UserCreationRequestDTO user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @PutMapping("/edit/{username}")
    public ResponseEntity<UserResponseDTO> EditUser(@RequestBody UserEditRequestDTO userEdited, @PathVariable String username) {
        UserResponseDTO user = userService.update(userEdited, username);
            return ResponseEntity.ok(user);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<UserResponseDTO> deleteUser(@PathVariable Long id) {
        Optional<UserResponseDTO> userOptional = userService.findById(id);
        if (userOptional.isPresent()) {

            UserResponseDTO user = userOptional.get();
            String previousPhoto = user.getProfilePhoto();
            if (previousPhoto != null && (!previousPhoto.isEmpty())) {
                Path previousPhotoRoute = Paths.get("uploads").resolve(previousPhoto).toAbsolutePath();
                File previousPhotoFile = previousPhotoRoute.toFile();
                if (previousPhotoFile.exists() && previousPhotoFile.canRead()) {
                    previousPhotoFile.delete();
                }
            }

            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/uploadProfileImage")
    public ResponseEntity<Map<String, Object>> uploadProfileImage(@RequestParam("file") MultipartFile file) {
        return userService.uploadProfileImage(file);
    }

    @GetMapping("/uploads/profile/img/{photoName:.+}")
    public ResponseEntity<Resource> viewProfilePhoto(@PathVariable String photoName) {
        Path fileRoute = Paths.get("uploads/profile").resolve(photoName).toAbsolutePath();
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

    @PostMapping("/uploadCoverImage")
    public ResponseEntity<?> uploadCoverImage(@RequestParam("file") MultipartFile file) {
        return userService.uploadCoverImage(file);
    }

    @GetMapping("/uploads/cover/img/{photoName:.+}")
    public ResponseEntity<Resource> viewCoverPhoto(@PathVariable String photoName) {
        Path fileRoute = Paths.get("uploads/cover").resolve(photoName).toAbsolutePath();
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

    @GetMapping("/followers/{username}")
    public List<UserFollow> getFollowersByUsername(@PathVariable String username){
        return userService.findFollowersByUsername(username);
    }

    @GetMapping("/following/{username}")
    public List<UserFollow> getFollowedsByUsername(@PathVariable String username){
        return userService.findFollowedsByFollower(username);
    }

    @GetMapping("/logged-user")
    public UserResponseDTO getLoggedUser(){
        return userService.getLoggedUser();
    }
}
