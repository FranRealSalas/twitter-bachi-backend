package com.twitter.bachi.backend.twitter_bachi_backend.controller;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import com.twitter.bachi.backend.twitter_bachi_backend.model.UserRequest;
import com.twitter.bachi.backend.twitter_bachi_backend.service.UserService;
import org.slf4j.ILoggerFactory;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getUser(@PathVariable Long id) {
        Optional<User> userOptional = userService.findById(id);
        if (userOptional.isPresent()){
            return ResponseEntity.status(HttpStatus.OK).body(userOptional.orElseThrow());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("error", "El Usuario no se encontro por el id" + id));
    }

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user){
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> EditUser(@RequestBody UserRequest user, @PathVariable Long id){
        Optional <User> userOptional = userService.update(user, id);
        if (userOptional.isPresent()){
            return ResponseEntity.ok(userOptional.orElseThrow());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id){
        Optional <User> userOptional = userService.findById(id);
        if (userOptional.isPresent()){

            User user = userOptional.get();
            String previousPhoto = user.getProfilePhoto();
            if (previousPhoto != null  && (!previousPhoto.isEmpty())){
                Path previousPhotoRoute = Paths.get("uploads").resolve(previousPhoto).toAbsolutePath();
                File previousPhotoFile = previousPhotoRoute.toFile();
                if (previousPhotoFile.exists() && previousPhotoFile.canRead()){
                    previousPhotoFile.delete();
                }
            }

            userService.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("file")MultipartFile file, @RequestParam("id") Long id){
        Map<String, Object> response = new HashMap<>();

        Optional<User> optionalUser = userService.findById(id);

        if (!optionalUser.isPresent()) {
            response.put("message", "Usuario no encontrado");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        if (!file.isEmpty()){
            String fileName = UUID.randomUUID().toString();
            Path fileRoute = Paths.get("uploads").resolve(fileName).toAbsolutePath();

            try{
                Files.copy(file.getInputStream(), fileRoute);
            } catch (IOException e) {
                response.put("message", "Error al subir la imagen");
                response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String previousPhoto = user.getProfilePhoto();
            if (previousPhoto != null  && !previousPhoto.isEmpty()){
                Path previousPhotoRoute = Paths.get("uploads").resolve(previousPhoto).toAbsolutePath();
                File previousPhotoFile = previousPhotoRoute.toFile();
                if (previousPhotoFile.exists() && previousPhotoFile.canRead()){
                    previousPhotoFile.delete();
                }
            }

            user.setProfilePhoto(fileName);
            userService.save(user);

            response.put("user", user);
            response.put("message","imagen subida correctamente");
        }

        return new ResponseEntity<Map<String, Object>> (response, HttpStatus.CREATED);
    }

    @GetMapping("/uploads/img/{photoName:.+}")
    public ResponseEntity<Resource> viewPhoto(@PathVariable String photoName){
        Path fileRoute = Paths.get("uploads").resolve(photoName).toAbsolutePath();
        Resource resource = null;

        try {
            resource = new UrlResource(fileRoute.toUri());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        HttpHeaders header = new HttpHeaders();
        assert resource != null;
        header.add(HttpHeaders.CONTENT_DISPOSITION, "Attachment; filename=\"" + resource.getFilename() + "\"");

        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    }
}
