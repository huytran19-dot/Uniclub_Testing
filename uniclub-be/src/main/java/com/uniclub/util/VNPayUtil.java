package com.uniclub.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class VNPayUtil {
    
    /**
     * Build URL query string from params map (sorted by key)
     */
    public static String buildQueryString(Map<String, String> params) {
        StringBuilder query = new StringBuilder();
        
        // Sort params by key
        Map<String, String> sortedParams = new TreeMap<>(params);
        
        for (Map.Entry<String, String> entry : sortedParams.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                if (query.length() > 0) {
                    query.append("&");
                }
                try {
                    query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8.toString()));
                    query.append("=");
                    query.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8.toString()));
                } catch (UnsupportedEncodingException e) {
                    throw new RuntimeException("Error encoding URL parameter", e);
                }
            }
        }
        
        return query.toString();
    }
    
    /**
     * Generate HMAC SHA512 secure hash
     */
    public static String hmacSHA512(String key, String data) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA512");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            hmac.init(secretKey);
            byte[] hashBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            
            // Convert byte array to hex string
            StringBuilder result = new StringBuilder();
            for (byte b : hashBytes) {
                result.append(String.format("%02x", b));
            }
            
            return result.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Error generating HMAC SHA512", e);
        }
    }
    
    /**
     * Validate VNPay secure hash
     */
    public static boolean validateSecureHash(Map<String, String> params, String hashSecret, String secureHash) {
        // Remove vnp_SecureHash from params
        Map<String, String> paramsToHash = new TreeMap<>(params);
        paramsToHash.remove("vnp_SecureHash");
        paramsToHash.remove("vnp_SecureHashType");
        
        // Build hash data string
        String hashData = buildQueryString(paramsToHash);
        
        // Generate hash
        String calculatedHash = hmacSHA512(hashSecret, hashData);
        
        return calculatedHash.equalsIgnoreCase(secureHash);
    }
}
