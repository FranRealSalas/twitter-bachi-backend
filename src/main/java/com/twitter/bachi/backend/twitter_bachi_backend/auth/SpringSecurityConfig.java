package com.twitter.bachi.backend.twitter_bachi_backend.auth;

import com.twitter.bachi.backend.twitter_bachi_backend.auth.filter.JWTValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SpringSecurityConfig {

    @Autowired
    private JWTValidationFilter jwtValidationFilter;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder builder = http.getSharedObject(AuthenticationManagerBuilder.class);
        builder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
        AuthenticationManager authenticationManager = builder.build();

        return http.authorizeHttpRequests(authz ->
                        authz.
                                requestMatchers(HttpMethod.GET, "/api/users")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/users")
                                .permitAll()
                                .requestMatchers(HttpMethod.PUT, "/api/users/edit/{username}")
                                .permitAll()
                                .requestMatchers(HttpMethod.DELETE, "/api/users/{id}")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/uploads/profile/img/{photoName:.+}")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/tweets/uploads/tweetImages/{photoName}")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/users/uploads/cover/img/{photoName:.+}")
                                .permitAll()
                                .requestMatchers("/ws/**")
                                .permitAll()
                                .requestMatchers("/api/auth/login")
                                .permitAll()
                                .requestMatchers("/error")
                                .permitAll()
                                .anyRequest()
                                .authenticated()
                )
                .authenticationManager(authenticationManager)
                .cors(Customizer.withDefaults())
                .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("*"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
