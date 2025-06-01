package com.example.Backend_IE303.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        // Cho phép frontend truy cập
        config.addAllowedOrigin("http://localhost:5173");
        config.addAllowedOrigin("https://frontend-ie-303.vercel.app");

        // Cho phép các phương thức HTTP
        config.addAllowedMethod("*");

        // Cho phép các header
        config.addAllowedHeader("*");

        // Cho phép gửi credentials (cookies, authorization headers)
        config.setAllowCredentials(true);

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
