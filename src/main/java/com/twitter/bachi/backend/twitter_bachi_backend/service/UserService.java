package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.UserCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.UserEditRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.UserResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    List<UserResponseDTO> findAll();

    Optional<UserResponseDTO> findById(Long id);

    UserResponseDTO save(UserCreationRequestDTO user);

    UserResponseDTO update(UserEditRequestDTO user, Long id);

    void deleteById(Long id);

    Optional<UserResponseDTO> findByUsername(String username);

    void giveFollow(String username);

    void removeFollow(String username);

    ResponseEntity<Map<String, Object>> uploadProfileImage(MultipartFile file);

    ResponseEntity<Map<String, Object>> uploadCoverImage(MultipartFile file);
}
