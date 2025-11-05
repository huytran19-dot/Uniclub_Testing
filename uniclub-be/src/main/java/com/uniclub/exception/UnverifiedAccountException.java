package com.uniclub.exception;

/**
 * Exception thrown when user tries to login with unverified account
 * This exception signals that a verification code should be sent
 */
public class UnverifiedAccountException extends RuntimeException {
    private final String email;
    
    public UnverifiedAccountException(String email, String message) {
        super(message);
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
}
