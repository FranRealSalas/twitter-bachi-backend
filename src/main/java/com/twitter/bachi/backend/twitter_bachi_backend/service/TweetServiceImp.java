package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.mapper.TweetMapper;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.TweetCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.TweetEditRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.TweetResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.*;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

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

    @Autowired
    private ImageService imageService;

    @Autowired
    private TweetImageRepository tweetImageRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserNotificationRepository userNotificationRepository;

    @Transactional(readOnly = true)
    @Override
    public List<TweetResponseDTO> findAll(Long id) {
        return this.repository.findAllByParentTweetIsNullOrderByIdDesc(id, 10).stream().map(tweet -> tweetMapper.toDto(tweet)).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<TweetResponseDTO> findById(Long id) {
        return repository.findById(id).map(tweet -> tweetMapper.toDto(tweet));
    }

    @Transactional
    @Override
    public TweetResponseDTO save(TweetCreationRequestDTO tweetDTO, MultipartFile[] images) {
        Tweet tweet = tweetMapper.toEntity(tweetDTO);
        List<Image> imagesList = new ArrayList<>();

        if (images != null) {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String fileName = UUID.randomUUID().toString() + image.getName();
                    File f = new File("uploads/tweetImages").getAbsoluteFile();
                    f.mkdirs();
                    Path fileRoute = Paths.get("uploads/tweetImages").resolve(fileName).toAbsolutePath();

                    try {
                        Files.copy(image.getInputStream(), fileRoute, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    Image image1 = new Image();
                    image1.setImageName(fileName);
                    imageService.save(image1);
                    imagesList.add(image1);
                }
            }
        }
        tweet.setUser(userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow());
        tweet.setImages(imagesList);

        return tweetMapper.toDto(this.repository.save(tweet));
    }

    @Override
    public TweetResponseDTO edit(TweetEditRequestDTO tweet, Long id) {
        Optional<Tweet> tweetOptional = repository.findById(id);
        if (tweetOptional.isPresent()) {
            Tweet tweetDB = tweetOptional.get();
            tweetMapper.toEntity(tweet, tweetDB);

            if (tweetDB.getUser().getUsername().equals((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal())) {
                return tweetMapper.toDto(repository.save(tweetDB));
            }
        }
        throw new RuntimeException("Tweet not found");
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public List<TweetResponseDTO> findCommentsByParentId(Long parentId, Long id) {
        return repository.findByParentTweet_Id(parentId, 10, id).stream().map(tweet -> tweetMapper.toDto(tweet)).toList();
    }

    @Override
    public int countCommentsByParentId(Long parentId) {
        return repository.countByParentTweet_Id(parentId);
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

                if (!tweetDB.getUser().getUsername().equals(user.getUsername())) {
                    Notification notification = new Notification();
                    notification.setIcon("like_icon");
                    notification.setIconType("image/png");
                    notification.setHref("/tweets/" + tweetDB.getId());
                    notification.setProfileImage(user.getProfilePhoto());
                    notification.setDescription(user.getUsername() + " indicó que le gusta tu post");
                    notification.setDate(new Date());
                    notificationRepository.save(notification);

                    UserNotification userNotification = new UserNotification();
                    userNotification.setUser(tweetDB.getUser());
                    userNotification.setNotification(notification);
                    userNotificationRepository.save(userNotification);
                }
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
    public List<TweetResponseDTO> getTweetsSavedByUsername(String username, Long id) {
        return tweetSaveRepository.findByUser_username(username, 10, id).stream().map(tweet -> tweetMapper.toDto(tweet.getTweet())).toList();
    }

    @Override
    public List<TweetResponseDTO> getTweetsLikedByUsername(String username, Long id) {
        return tweetLikeRepository.findByUser_username(username, 5, id).stream().map(tweet -> tweetMapper.toDto(tweet.getTweet())).toList();
    }

    @Override
    public List<TweetResponseDTO> getTweetsByUsername(String username, Long id) {
        return this.repository.findByUser_usernameOrderByIdDesc(username, 10, id).stream().map(tweet -> tweetMapper.toDto(tweet)).toList();
    }

    @Override
    public List<TweetResponseDTO> getCommentsByUsername(String username, Long id) {
        return repository.findByUser_usernameAndParentTweetNotNull(username, 5, id).stream().map(tweet -> tweetMapper.toDto(tweet)).toList();
    }

    @Override
    public List<TweetResponseDTO> getTweetsWithImagesByUsername(String username, Long id) {
        return repository.findByUser_usernameAndImagesNotNull(username, 5, id).stream().map(tweet -> tweetMapper.toDto(tweet)).toList();
    }

    @Override
    public List<TweetResponseDTO> getTweetsByFolloweds() {
        List<UserFollow> followeds = userFollowRepository.findByFollower_username((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return repository.findByFolloweds(followeds.stream().map(UserFollow::getFollowed).toList()).stream().map(tweet -> tweetMapper.toDto(tweet)).toList();
    }
}