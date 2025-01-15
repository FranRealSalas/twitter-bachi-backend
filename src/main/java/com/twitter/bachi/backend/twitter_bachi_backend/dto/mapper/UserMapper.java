package com.twitter.bachi.backend.twitter_bachi_backend.dto.mapper;

import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.UserCreationRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.UserEditRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.UserResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.entity.User;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.UserFollowRepository;
import com.twitter.bachi.backend.twitter_bachi_backend.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public abstract class UserMapper {
    @Autowired
    protected UserRepository userRepository;

    @Autowired
    protected UserFollowRepository userFollowRepository;

    public abstract User toEntity(UserCreationRequestDTO userDTO);

    public abstract User toEntity(UserEditRequestDTO userDTO, @MappingTarget User user);

    @Mapping(target = "follow", expression = "java(isFollow(user))")
    @Mapping(target = "followedCount", expression = "java(followedCountByUser(user))")
    @Mapping(target = "followerCount", expression = "java(followerCountByUser(user))")
    public abstract UserResponseDTO toDto(User user);

    public boolean isFollow(User followed){
        if (SecurityContextHolder.getContext().getAuthentication() instanceof AnonymousAuthenticationToken) {
            return false;
        }
        User follower = userRepository.findByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).orElseThrow();

        return userFollowRepository.existsByFollowerAndFollowed(follower, followed);
    }

    public long followedCountByUser(User user){
        return userFollowRepository.countByFollowed(user);
    }

    public long followerCountByUser(User user){
        return userFollowRepository.countByFollower(user);
    }
}
