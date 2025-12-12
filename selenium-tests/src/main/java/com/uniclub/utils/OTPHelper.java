package com.uniclub.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 * Helper class to get OTP code for testing
 * Uses backend test API to retrieve verification codes
 */
public class OTPHelper {
    
    /**
     * Get OTP from backend test API
     * Calls GET /api/test/verification-code?email={email}
     */
    public static String getOTPForEmail(String email) {
        try {
            String backendUrl = ConfigReader.getBackendUrl();
            String apiUrl = backendUrl + "/api/test/verification-code?email=" + email;
            URI uri = new URI(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) uri.toURL().openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            
            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = in.readLine()) != null) {
                    response.append(line);
                }
                in.close();
                
                // Parse JSON response
                JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
                String code = jsonResponse.get("code").getAsString();
                System.out.println("✅ Retrieved OTP for " + email + ": " + code);
                return code;
            } else {
                System.out.println("❌ Failed to get OTP. Response code: " + responseCode);
                return null;
            }
        } catch (Exception e) {
            System.out.println("❌ Error getting OTP: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Get OTP from backend logs (development only)
     * This reads the console output from backend
     */
    public static String getOTPFromLogs(String email) {
        // This would require access to backend logs
        // Not implemented for security reasons
        return null;
    }
    
    /**
     * Generate test OTP (for backend mock/test mode)
     */
    public static String generateTestOTP() {
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
    
    /**
     * Wait and retry to get OTP from backend API
     */
    public static String waitAndGetOTP(String email, int maxRetries, int delayMs) {
        for (int i = 0; i < maxRetries; i++) {
            String otp = getOTPForEmail(email);
            if (otp != null) {
                return otp;
            }
            try {
                Thread.sleep(delayMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return null;
    }
}
