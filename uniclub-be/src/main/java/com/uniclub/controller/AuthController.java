package com.uniclub.controller;

import com.uniclub.dto.request.Auth.LoginRequest;
import com.uniclub.dto.request.User.RegisterRequest;
import com.uniclub.dto.request.User.VerifyCodeRequest;
import com.uniclub.dto.response.Auth.LoginResponse;
import com.uniclub.dto.response.User.UserResponse;
import com.uniclub.service.AuthService;
import com.uniclub.service.UserService;
import com.uniclub.service.VerificationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Validated
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserService userService;

    @Autowired
    private VerificationService verificationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @RequestBody RegisterRequest request) {
        try {
            UserResponse user = userService.registerNewUser(request);
            return ResponseEntity.ok("Registration successful! Please check your email for verification code.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/verify-code")
    public ResponseEntity<String> verifyCode(@Valid @RequestBody VerifyCodeRequest request) {
        try {
            boolean isValid = userService.verifyUserByCode(request);
            if (isValid) {
                return ResponseEntity.ok("Account verified successfully. You can now log in.");
            } else {
                return ResponseEntity.badRequest().body("Invalid or expired verification code.");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/resend-code")
    public ResponseEntity<String> resendCode(@RequestParam String email) {
        try {
            userService.resendVerificationCode(email);
            return ResponseEntity.ok("Verification code has been resent to your email.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authService.login(loginRequest);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
