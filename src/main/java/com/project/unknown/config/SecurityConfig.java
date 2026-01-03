package com.project.unknown.config;

import com.project.unknown.auth.JwtAuthenticationEntryPoint;
import com.project.unknown.auth.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> {})
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(authenticationEntryPoint))
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/verify-email").permitAll()
                        .requestMatchers("/api/resend-verification").permitAll()
                        .requestMatchers("/api/forgot-password").permitAll()
                        .requestMatchers("/api/reset-password").permitAll()
                        .requestMatchers("/uploads/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/posts/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/reactions/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/users/{id}/profile").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/search/**").permitAll()

                        .requestMatchers("/api/admin/users/*/promote-to-admin").hasRole("SUPER_ADMIN")
                        .requestMatchers("/api/admin/users/*/demote-to-user").hasRole("SUPER_ADMIN")

                        .requestMatchers("/api/admin/**").hasAnyRole("ADMIN", "SUPER_ADMIN")

                        .anyRequest().authenticated());


        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
