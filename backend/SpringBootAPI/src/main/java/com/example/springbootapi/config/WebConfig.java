package com.example.springbootapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


// generated Class to allow localhost to access API

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Erlaubt CORS für alle Endpunkte
                        .allowedOrigins("http://127.0.0.1:5500") // Erlaubt nur von diesem Ursprung
                        .allowedMethods("GET")//, "POST", "PUT", "DELETE", "OPTIONS") // Erlaubte HTTP-Methoden
                        .allowedHeaders("*") // Erlaubt alle Header
                        .allowCredentials(true);
            }
        };
    }
}
