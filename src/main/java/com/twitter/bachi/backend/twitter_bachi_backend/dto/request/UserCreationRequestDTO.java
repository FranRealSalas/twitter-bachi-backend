package com.twitter.bachi.backend.twitter_bachi_backend.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserCreationRequestDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @Email
    private String email;

}
