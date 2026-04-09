package com.shyamsarkar.buildingmaterials.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.shyamsarkar.buildingmaterials.filter.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // ❌ Disable CSRF (JWT is stateless)
            .csrf(csrf -> csrf.disable())

            // ❌ Disable session (JWT only)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // ✅ Authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers(
                    "/api/auth/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/h2-console/**",
                    "/ws/**"
                ).permitAll()

                // Admin only
                .requestMatchers("/api/admin/**").hasRole("ADMIN")

                // Customer only
                .requestMatchers("/api/customers/**").hasAnyRole("CUSTOMER","ADMIN","DRIVER")

                // Driver only

                .requestMatchers("/api/driver/**").hasRole("DRIVER")

                .requestMatchers("/images/**").permitAll()

                // Everything else needs authentication
                .anyRequest().authenticated()
            )

            // ✅ Add JWT filter
            .addFilterBefore(
                jwtAuthenticationFilter,
                UsernamePasswordAuthenticationFilter.class
            )

            // ✅ Allow H2 console frames
            .headers(headers ->
                headers.frameOptions(frame -> frame.disable())
            );

        return http.build();
    }

    // ✅ Password encoder bean (used everywhere)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ AuthenticationManager (optional but recommended)
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config
    ) throws Exception {
        return config.getAuthenticationManager();
    }
}
