package com.twitter.bachi.backend.twitter_bachi_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponseDTO {

    private Long id;

    private String editableName;

    private String username;

    private Long countTweets;

    private String email;

    private String profilePhoto;

    private String coverPhoto;

    private boolean follow;

    //SEGUIDOS
    private long followerCount;

    //SEGUIDORES
    private long followedCount;
}
