package dev.cyborg.task_management.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    @Value("${API_BASE_URL:/api}")
    private String baseUrl;

    public String getBaseUrl() {
        return baseUrl;
    }
}
