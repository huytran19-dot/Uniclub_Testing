package com.uniclub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.uniclub.service.VerificationService;

import java.util.HashMap;
import java.util.Map;

/**
 * TEST ONLY Controller
 * Provides endpoints to get verification codes for testing
 * Only enabled when app.testing.enabled=true
 */
@RestController
@RequestMapping("/api/test")
@ConditionalOnProperty(name = "app.testing.enabled", havingValue = "true")
@CrossOrigin(origins = "*")
public class TestController {

    @Autowired
    private VerificationService verificationService;

    /**
     * Get verification code for an email (TEST ONLY)
     * This should NEVER be enabled in production
     */
    @GetMapping("/verification-code")
    public ResponseEntity<?> getVerificationCode(@RequestParam String email) {
        String code = verificationService.getStoredCode(email);

        if (code != null) {
            Map<String, Object> response = new HashMap<>();
            response.put("email", email);
            response.put("code", code);
            response.put("message", "Verification code retrieved for testing");
            return ResponseEntity.ok(response);
        } else {
            Map<String, String> error = new HashMap<>();
            error.put("error", "No verification code found for email: " + email);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Health check for test endpoints
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> testHealth() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "Test endpoints are active");
        response.put("warning", "These endpoints should only be enabled in test environment");
        return ResponseEntity.ok(response);
    }
}
