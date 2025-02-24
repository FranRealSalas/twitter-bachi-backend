package com.twitter.bachi.backend.twitter_bachi_backend.repository;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TweetImageRepository extends JpaRepository <Image, Long> {
}
