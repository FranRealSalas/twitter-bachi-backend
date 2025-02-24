package com.twitter.bachi.backend.twitter_bachi_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.request.LoginRequestDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.dto.response.UserResponseDTO;
import com.twitter.bachi.backend.twitter_bachi_backend.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.twitter.bachi.backend.twitter_bachi_backend.auth.TokenJWTConfig.SECRET_KEY;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword());
        try {
            Authentication authResult = this.authenticationManager.authenticate(authenticationToken);
            User user = (org.springframework.security.core.userdetails.User) authResult.getPrincipal();
            String username = user.getUsername();

            Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();

            boolean isAdmin = roles.stream().anyMatch(role -> role.getAuthority().equals("Admin"));

            Optional<UserResponseDTO> userData =  userService.findByUsername(username);

            if (userData.isPresent()) {
                String profileImage = userData.get().getProfilePhoto();
                String coverImage = userData.get().getCoverPhoto();
                String editableName = userData.get().getEditableName();

                Claims claims = Jwts
                        .claims()
                        .add("authorities", new ObjectMapper().writeValueAsString(roles))
                        .add("username", userData.get().getUsername())
                        .add("profilePhoto", profileImage)
                        .add("coverPhoto", coverImage)
                        .add("editableName", editableName)
                        .build();

                String jwt = Jwts.builder()
                        .subject(username)
                        .claims(claims)
                        .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                        .issuedAt(new Date())
                        .expiration(new Date(System.currentTimeMillis() + 3600000))
                        .compact();

                Map<String, String> body = new HashMap<>();
                body.put("token", jwt);
                body.put("username", username);
                body.put("message", String.format("Iniciaste sesion con exito %s", username));
                return new ResponseEntity<>(body, HttpStatus.OK);
            }else{
                throw new RuntimeException("User not found");
            }

        } catch (Exception e) {
            Map<String, String> body = new HashMap<>();
            body.put("message", "Error en la autenticacion");
            body.put("error", e.getMessage());
            return new ResponseEntity<>(body, HttpStatus.UNAUTHORIZED);
        }
    }
}
