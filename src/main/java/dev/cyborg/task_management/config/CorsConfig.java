package dev.cyborg.task_management.config;

import jakarta.annotation.Nonnull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(@Nonnull CorsRegistry registry) {
                registry.addMapping("/**")  // Allow all paths
                        .allowedOrigins("http://localhost:4200") // Allow frontend origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow methods
                        .allowedHeaders("*") // Allow all headers
                        .allowCredentials(true); // Allow cookies/authentication
            }
        };
    }
}
