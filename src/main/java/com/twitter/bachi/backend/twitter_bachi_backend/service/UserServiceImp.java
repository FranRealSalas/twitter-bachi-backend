package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.mapper.UserMapper;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.UserCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.UserEditRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.UserResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Notification;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.UserFollow;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.UserNotification;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.NotificationRepository;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.UserFollowRepository;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.UserNotificationRepository;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@Service
public class UserServiceImp implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRepository repository;

    @Autowired
    private UserFollowRepository userFollowRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Override
    @Transactional(readOnly = true)
    public List<UserResponseDTO> findAll() {
        return this.repository.findAll().stream().map(user -> userMapper.toDto(user)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserResponseDTO> findById(Long id) {
        return repository.findById(id).map(user -> userMapper.toDto(user));
    }

    @Override
    @Transactional
    public UserResponseDTO save(UserCreationRequestDTO userDTO) {
        User user = userMapper.toEntity(userDTO);

        return userMapper.toDto(this.repository.save(user));
    }

    @Override
    @Transactional
    public UserResponseDTO update(UserEditRequestDTO user, String username) {
        Optional<User> userOptional = repository.findByUsername(username);
        if (userOptional.isPresent()) {
            User userDB = userOptional.get();
            userMapper.toEntity(user, userDB);

            return userMapper.toDto(repository.save(userDB));
        }
        throw new RuntimeException("User not found");
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<UserResponseDTO> findByUsername(String username) {
        return repository.findByUsername(username).map(user -> userMapper.toDto(user));
    }

    @Override
    public void giveFollow(String username) {
        if (((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).equals(username)) {
            throw new IllegalArgumentException("No puedes seguirte a ti mismo");
        }

        User follower = repository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();
        UserFollow userFollow = new UserFollow();
        userFollow.setFollower(follower);

        Optional<User> userOptional = repository.findByUsername(username);
        if (userOptional.isPresent()) {
            User userDB = userOptional.get();
            userFollow.setFollowed(userDB);

            Optional<UserFollow> optionalUserFollow = userFollowRepository.findByFollowerAndFollowed(follower, userDB);

            if (optionalUserFollow.isEmpty()) {
                userFollowRepository.save(userFollow);

                if (!userDB.getUsername().equals(follower.getUsername())) {
                    Notification notification = new Notification();
                    notification.setIcon("follow_icon");
                    notification.setIconType("image/png");
                    notification.setHref("/users/" + userFollow.getFollower().getUsername());
                    notification.setProfileImage(userFollow.getFollower().getProfilePhoto());
                    notification.setDescription(userFollow.getFollower().getUsername() + " te siguió");
                    notification.setDate(new Date());
                    notificationRepository.save(notification);

                    UserNotification userNotification = new UserNotification();
                    userNotification.setUser(userDB);
                    userNotification.setNotification(notification);
                    userNotificationRepository.save(userNotification);
                }
            }
        }
    }

    @Override
    public void removeFollow(String username) {
        User follower = repository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();

        Optional<User> userOptional = repository.findByUsername(username);
        if (userOptional.isPresent()) {
            User userDB = userOptional.get();
            Optional<UserFollow> optionalUserFollow = userFollowRepository.findByFollowerAndFollowed(follower, userDB);

            if (optionalUserFollow.isPresent()) {
                userFollowRepository.deleteById(optionalUserFollow.get().getId());
            }
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> uploadProfileImage(MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> optionalUser = repository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if (!optionalUser.isPresent()) {
            response.put("message", "Usuario no encontrado");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        if (!file.isEmpty()) {
            String fileName = user.getUsername();
            File f = new File("uploads/profile").getAbsoluteFile();
            f.mkdirs();
            Path fileRoute = Paths.get("uploads/profile").resolve(fileName).toAbsolutePath();

            try {
                Files.copy(file.getInputStream(), fileRoute, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                response.put("message", "Error al subir la imagen");
                response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            user.setProfilePhoto(fileName);
            repository.save(user);

            response.put("user", user);
            response.put("message", "imagen subida correctamente");
        }

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<Map<String, Object>> uploadCoverImage(@RequestParam("file") MultipartFile file) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> optionalUser = repository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if (!optionalUser.isPresent()) {
            response.put("message", "Usuario no encontrado");
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();

        if (!file.isEmpty()) {
            String fileName = user.getUsername();
            File f = new File("uploads/cover").getAbsoluteFile();
            f.mkdirs();
            Path fileRoute = Paths.get("uploads/cover").resolve(fileName).toAbsolutePath();

            try {
                Files.copy(file.getInputStream(), fileRoute, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
                response.put("message", "Error al subir la imagen");
                response.put("error", e.getMessage().concat(": ").concat(e.getCause().getMessage()));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
            }

            user.setCoverPhoto(fileName);
            repository.save(user);

            response.put("user", user);
            response.put("message", "imagen subida correctamente");
        }

        return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
    }

    @Override
    public List<UserFollow> findFollowedsByFollower(String username) {
        return userFollowRepository.findByFollower_username(username);
    }

    @Override
    public List<UserFollow> findFollowersByUsername(String username) {
        return userFollowRepository.findByFollowed_username(username);
    }

    @Override
    public UserResponseDTO getLoggedUser() {
        return userMapper.toDto(repository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow());
    }
}