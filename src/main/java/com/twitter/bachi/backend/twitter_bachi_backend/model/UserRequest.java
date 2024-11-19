package com.twitter.bachi.backend.twitter_bachi_backend.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Transient;

public class UserRequest implements IUser{

    private String username;

    private String email;

    private boolean admin;

    public boolean isAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
