package com.uniclub.service.impl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import com.uniclub.service.VerificationService;

/**
 * Implementation of VerificationService
 * In-memory verification code storage service
 * Used to store verification codes temporarily for email verification after registration
 */
@Service
public class VerificationServiceImpl implements VerificationService {

    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    @Value("${app.mail.from}")
    private String fromEmail;

    // Store: email -> VerificationData
    private final Map<String, VerificationData> codeStorage = new ConcurrentHashMap<>();

    /**
     * Generate and store a 6-digit verification code for an email
     */
    @Override
    public String generateAndStoreCode(String email) {
        String code = String.format("%06d", (int) (Math.random() * 1000000));
        VerificationData data = new VerificationData(code, LocalDateTime.now().plusMinutes(15));
        codeStorage.put(email.toLowerCase(), data);

        // Try to send email via SendGrid
        System.out.println("========================================");
        System.out.println("📧 Attempting to send verification email to: " + email);
        System.out.println("Verification Code: " + code);
        System.out.println("Valid until: " + data.getExpiresAt());
        System.out.println("========================================");

        try {
            sendVerificationEmail(email, code);
            System.out.println("✅ Email sent successfully via SendGrid!");
        } catch (Exception e) {
            System.err.println("❌ Failed to send email via SendGrid");
            System.err.println("Error: " + e.getMessage());
            System.err.println("⚠️  Note: Verification code is still valid for 15 minutes");
            System.err.println("💡 User can still verify using the code: " + code);
            System.err.println("========================================");
            // Don't throw exception - allow registration to continue with console code
        }

        return code;
    }

    /**
     * Send verification email using SendGrid REST API
     */
    private void sendVerificationEmail(String toEmail, String code) throws IOException {
        Email from = new Email(fromEmail, "UniClub");
        Email to = new Email(toEmail);
        String subject = "UniClub - Email Verification Code";
        
        String emailContent = 
            "Welcome to UniClub!\n\n" +
            "Your verification code is: " + code + "\n\n" +
            "This code will expire in 15 minutes.\n\n" +
            "If you didn't request this code, please ignore this email.\n\n" +
            "Best regards,\n" +
            "UniClub Team";
        
        Content content = new Content("text/plain", emailContent);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        
        request.setMethod(Method.POST);
        request.setEndpoint("mail/send");
        request.setBody(mail.build());
        
        Response response = sg.api(request);
        
        // Log response for debugging
        System.out.println("SendGrid Response Status: " + response.getStatusCode());
        if (response.getStatusCode() >= 400) {
            throw new IOException("SendGrid API error: " + response.getBody());
        }
    }

    /**
     * Verify the code for an email
     * Returns true if valid, false if invalid or expired
     */
    @Override
    public boolean verifyCode(String email, String code) {
        String key = email.toLowerCase();
        VerificationData data = codeStorage.get(key);

        if (data == null) {
            return false; // No code found
        }

        // Check expiration
        if (LocalDateTime.now().isAfter(data.getExpiresAt())) {
            codeStorage.remove(key); // Remove expired code
            return false;
        }

        // Check code match
        if (!data.getCode().equals(code)) {
            return false;
        }

        // Valid code - remove it (one-time use)
        codeStorage.remove(key);
        return true;
    }

    /**
     * Check if a verification code exists for an email
     */
    @Override
    public boolean hasCode(String email) {
        String key = email.toLowerCase();
        VerificationData data = codeStorage.get(key);

        if (data == null) {
            return false;
        }

        // Remove if expired
        if (LocalDateTime.now().isAfter(data.getExpiresAt())) {
            codeStorage.remove(key);
            return false;
        }

        return true;
    }

    /**
     * Remove verification code for an email
     */
    @Override
    public void removeCode(String email) {
        codeStorage.remove(email.toLowerCase());
    }

    /**
     * Inner class to store verification data
     */
    private static class VerificationData {
        private final String code;
        private final LocalDateTime expiresAt;

        public VerificationData(String code, LocalDateTime expiresAt) {
            this.code = code;
            this.expiresAt = expiresAt;
        }

        public String getCode() {
            return code;
        }

        public LocalDateTime getExpiresAt() {
            return expiresAt;
        }
    }
}

