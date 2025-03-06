package com.twitter.bachi.backend.twitter_bachi_backend.service;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.Image;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.TweetImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ImageServiceImp implements ImageService {
    @Autowired
    private TweetImageRepository tweetImageRepository;

    @Override
    @Transactional
    public Image save(Image image) {
        return this.tweetImageRepository.save(image);
    }
}
