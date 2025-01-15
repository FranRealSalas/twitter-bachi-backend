package com.twitter.bachi.backend.twitter_bachi_backend.dto.mapper;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.TweetCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.TweetEditRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.TweetResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.Tweet;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.TweetLikeRepository;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.TweetRepository;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.TweetSaveRepository;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {UserMapper.class})
public abstract class TweetMapper {

    @Autowired
    protected TweetRepository tweetRepository;

    @Autowired
    protected TweetLikeRepository tweetLikeRepository;

    @Autowired
    protected TweetSaveRepository tweetSaveRepository;

    @Autowired
    protected UserRepository userRepository;

    @Mapping(target = "parentTweet", expression = "java(tweetDTO.getParentTweetId() != null ? tweetRepository.findById(tweetDTO.getParentTweetId()).orElse(null) : null)")
    public abstract Tweet toEntity(TweetCreationRequestDTO tweetDTO);

    public abstract Tweet toEntity(TweetEditRequestDTO tweetDTO, @MappingTarget Tweet tweet);

    @Mapping(target = "liked", expression = "java(isLiked(tweet))")
    @Mapping(target = "saved", expression = "java(isSaved(tweet))")
    public abstract TweetResponseDTO toDto(Tweet tweet);

    public boolean isLiked(Tweet tweet){
            User user = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();
            Optional<Tweet> tweetOptional = tweetRepository.findById(tweet.getId());
            if (tweetOptional.isPresent()){
                return tweetLikeRepository.existsByUserAndTweet(user, tweet);
            }
            return  false;
    }

    public boolean isSaved(Tweet tweet){
        User user = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();
        Optional<Tweet> tweetOptional = tweetRepository.findById(tweet.getId());
        if (tweetOptional.isPresent()){
            return tweetSaveRepository.existsByUserAndTweet(user, tweet);
        }
        return  false;
    }
}
