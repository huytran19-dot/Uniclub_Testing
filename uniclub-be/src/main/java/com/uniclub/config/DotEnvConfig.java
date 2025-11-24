package com.uniclub.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration class để load environment variables từ .env.local file
 * Chạy trước khi Spring Boot application khởi động
 */
@Slf4j
@Configuration
public class DotEnvConfig {

    @PostConstruct
    public void loadEnv() {
        try {
            // Tìm file .env.local - try multiple locations
            String[] possiblePaths = {
                ".env.local",                           // Từ uniclub-be directory
                "../.env.local"                         // Từ parent directory
            };
            
            String envFilePath = null;
            for (String path : possiblePaths) {
                if (Files.exists(Paths.get(path))) {
                    envFilePath = path;
                    break;
                }
            }
            
            if (envFilePath != null) {
                log.info("✅ Found .env.local file at: {}, loading environment variables...", envFilePath);
                
                // Đọc từng dòng trong .env.local file
                Files.lines(Paths.get(envFilePath))
                    .filter(line -> !line.isEmpty() && !line.startsWith("#"))
                    .forEach(line -> {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            String key = parts[0].trim();
                            String value = parts[1].trim();
                            
                            // Chỉ set nếu environment variable chưa tồn tại
                            if (System.getenv(key) == null) {
                                System.setProperty(key, value);
                                log.debug("Set property: {} from .env.local", key);
                            }
                        }
                    });
                
                log.info("✅ Environment variables loaded from .env.local");
            } else {
                log.warn("⚠️ .env.local file not found. Using system environment variables or default values.");
            }
            
        } catch (IOException e) {
            log.warn("⚠️ Error reading .env.local file: {}", e.getMessage());
        }
    }
}
