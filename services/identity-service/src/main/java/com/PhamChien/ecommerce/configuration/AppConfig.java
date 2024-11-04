package com.PhamChien.ecommerce.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AppConfig {

    private String[] PUBLIC_ENDPOINTS = {"/api/v1/auth/login", "/api/v1/auth/register", "/api/v1/auth/activate-account", "/api/v1/auth/verifyToken", "/api/v1/auth/refresh", "/api/v1/auth/forgot-password", "/api/v1/auth/reset-password", "/api/v1/auth/change-password"};

    @Value("${jwt.secretKey}")
    private String secretKey;

    private final PreFilter preFilter;
    private final AuthenticationConfig authenticationConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, PreFilter preFilter, AuthenticationProvider provider) throws Exception {
        http.cors(Customizer.withDefaults())
        .authorizeHttpRequests(requests -> {
            requests
                    .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                    .anyRequest().authenticated();
        })

                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationConfig.provider()).addFilterBefore(preFilter, UsernamePasswordAuthenticationFilter.class)
        ;
        // Bỏ qua CSRF
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE")
                        .allowedHeaders("*")
                        .maxAge(3600);
            }
        };
    }


}
