package com.uniclub.service;

public interface VerificationService {
    String generateAndStoreCode(String email);
    boolean verifyCode(String email, String code);
    boolean hasCode(String email);
    void removeCode(String email);
    String getStoredCode(String email); // For testing only
}
