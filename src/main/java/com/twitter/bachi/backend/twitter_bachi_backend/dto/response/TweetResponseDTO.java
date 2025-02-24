package com.twitter.bachi.backend.twitter_bachi_backend.dto.response;

import com.twitter.bachi.backend.twitter_bachi_backend.entity.Image;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TweetResponseDTO {

    private Long id;

    private String content;

    private UserResponseDTO user;

    private Long parentTweetId;

    private int countComments;

    private Boolean liked;

    private long likeCount;

    private Boolean saved;

    private long saveCount;

    private List<Image> images;

    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date date;
}
