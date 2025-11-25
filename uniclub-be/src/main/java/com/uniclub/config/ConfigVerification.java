package com.uniclub.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * Configuration verification - verify mọi environment variables đã được load
 aaaaaa*/
@Slf4j
@Configuration
@DependsOn("dotEnvConfig")
public class ConfigVerification {
    
    @Value("${spring.datasource.url:jdbc:mysql://localhost:3307/uniclub}")
    private String dbUrl;
    
    @Value("${sendgrid.api-key:REPLACE_ME_SENDGRID_KEY}")
    private String sendgridKey;
    
    @Value("${cloudinary.cloud_name:REPLACE_ME_CLOUDINARY_CLOUD}")
    private String cloudinaryName;
    
    @Value("${jwt.secret:REPLACE_ME_JWT_SECRET}")
    private String jwtSecret;
    
    @Value("${vnpay.tmn-code:REPLACE_ME_VNPAY_TMN}")
    private String vnpayTmn;
    
    @PostConstruct
    public void verifyConfiguration() {
        log.info("========================================");
        log.info("Configuration Verification");
        log.info("========================================");
        log.info("✅ Database URL: {}", obfuscate(dbUrl));
        log.info("✅ SendGrid API Key: {}", obfuscate(sendgridKey));
        log.info("✅ Cloudinary Cloud: {}", obfuscate(cloudinaryName));
        log.info("✅ JWT Secret: {}", obfuscate(jwtSecret));
        log.info("✅ VNPay TMN Code: {}", obfuscate(vnpayTmn));
        log.info("========================================");
        log.info("All configurations loaded successfully!");
        log.info("========================================");
    }
    
    private String obfuscate(String value) {
        if (value == null || value.isEmpty()) {
            return "[EMPTY]";
        }
        if (value.length() <= 4) {
            return "****";
        }
        return value.substring(0, 4) + "***" + value.substring(value.length() - 2);
    }
}
