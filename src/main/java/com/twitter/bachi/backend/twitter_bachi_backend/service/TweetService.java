package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.TweetCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.TweetEditRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.TweetResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface TweetService {
    List<TweetResponseDTO> findAll();

    Optional<TweetResponseDTO> findById(Long id);

    TweetResponseDTO save(TweetCreationRequestDTO tweet);

    TweetResponseDTO edit(TweetEditRequestDTO tweet, Long id);

    void deleteById(Long id);

    List<TweetResponseDTO> findCommentsByParentId(Long parentId);

    void giveLike(Long id);

    void removeLike(Long id);

    void giveSave(Long id);

    void removeSave(Long id);

    List<TweetResponseDTO> getTweetsSavedByUsername(String username);

    List<TweetResponseDTO> getTweetsByUsername(String username);
}
