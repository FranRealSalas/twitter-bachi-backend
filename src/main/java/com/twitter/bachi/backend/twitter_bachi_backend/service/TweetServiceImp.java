package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.mapper.TweetMapper;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.TweetCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.TweetEditRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.TweetResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.TweetLike;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Tweet;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.TweetSave;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TweetServiceImp implements TweetService {

    @Autowired
    private TweetMapper tweetMapper;

    @Autowired
    private TweetRepository repository;

    @Autowired
    private TweetLikeRepository tweetLikeRepository;

    @Autowired
    private TweetSaveRepository tweetSaveRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFollowRepository userFollowRepository;

    @Override
    @Transactional(readOnly = true)
    public List<TweetResponseDTO> findAll() {
        return this.repository.findAllByParentTweetIsNull().stream().map(tweet -> tweetMapper.toDto(tweet)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TweetResponseDTO> findById(Long id) {
        return repository.findById(id).map(tweet -> tweetMapper.toDto(tweet));
    }

    @Override
    @Transactional
    public TweetResponseDTO save(TweetCreationRequestDTO tweetDTO) {
        Tweet tweet = tweetMapper.toEntity(tweetDTO);
        tweet.setUser(userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow());

        return tweetMapper.toDto(this.repository.save(tweet));
    }

    @Override
    public TweetResponseDTO edit(TweetEditRequestDTO tweet, Long id) {
        Optional<Tweet> tweetOptional = repository.findById(id);
        if (tweetOptional.isPresent()) {
            Tweet tweetDB = tweetOptional.get();
            tweetMapper.toEntity(tweet, tweetDB);

            return tweetMapper.toDto(repository.save(tweetDB));
        }
        throw new RuntimeException("Tweet not found");
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<TweetResponseDTO> findCommentsByParentId(Long parentId){
        return repository.findByParentTweet_Id(parentId).stream().map(tweet -> tweetMapper.toDto(tweet)).toList();
    }

    @Override
    public void giveLike(Long id) {
        User user = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();
        TweetLike tweetLike = new TweetLike();
        tweetLike.setUser(user);

        Optional<Tweet> tweetOptional = repository.findById(id);
        if (tweetOptional.isPresent()) {
            Tweet tweetDB = tweetOptional.get();
            tweetLike.setTweet(tweetDB);

            Optional<TweetLike> optionalTweetLike = tweetLikeRepository.findByUserAndTweet(user, tweetDB);
            if (optionalTweetLike.isEmpty()) {
                tweetLikeRepository.save(tweetLike);
            }
        }
    }

    @Override
    public void removeLike(Long id) {
        User user = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();

        Optional<Tweet> tweetOptional = repository.findById(id);
        if (tweetOptional.isPresent()) {
            Tweet tweetDB = tweetOptional.get();
            Optional<TweetLike> optionalTweetLike = tweetLikeRepository.findByUserAndTweet(user, tweetDB);

            if (optionalTweetLike.isPresent()) {
                tweetLikeRepository.deleteById(optionalTweetLike.get().getId());
            }
        }
    }

    @Override
    public void giveSave(Long id) {
        User user = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();
        TweetSave tweetSave = new TweetSave();
        tweetSave.setUser(user);

        Optional<Tweet> tweetOptional = repository.findById(id);
        if (tweetOptional.isPresent()) {
            Tweet tweetDB = tweetOptional.get();
            tweetSave.setTweet(tweetDB);

            Optional<TweetSave> optionalTweetSave = tweetSaveRepository.findByUserAndTweet(user, tweetDB);
            if (optionalTweetSave.isEmpty()) {
                tweetSaveRepository.save(tweetSave);
            }
        }
    }

    @Override
    public void removeSave(Long id) {
        User user = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();

        Optional<Tweet> tweetOptional = repository.findById(id);
        if (tweetOptional.isPresent()) {
            Tweet tweetDB = tweetOptional.get();
            Optional<TweetSave> optionalTweetSave = tweetSaveRepository.findByUserAndTweet(user, tweetDB);

            if (optionalTweetSave.isPresent()) {
                tweetSaveRepository.deleteById(optionalTweetSave.get().getId());
            }
        }
    }

    @Override
    public List<TweetResponseDTO> getTweetsSavedByUsername(String username) {
        return tweetSaveRepository.findByUser_username(username).stream().map(tweet -> tweetMapper.toDto(tweet.getTweet())).toList();
    }
}