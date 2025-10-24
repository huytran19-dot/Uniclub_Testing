package com.uniclub.service;

import com.uniclub.dto.request.Auth.LoginRequest;
import com.uniclub.dto.response.Auth.LoginResponse;
import com.uniclub.entity.User;
import com.uniclub.repository.UserRepository;
import com.uniclub.config.JwtConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtConfig jwtConfig;

    public LoginResponse login(LoginRequest loginRequest) {
        // Find user by email
        Optional<User> userOptional = userRepository.findByEmail(loginRequest.getEmail());
        
        if (userOptional.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        User user = userOptional.get();

        // Check if user is active
        if (user.getStatus() != 1) {
            throw new RuntimeException("Account is deactivated");
        }

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // Generate JWT token
        String token = jwtConfig.generateToken(user.getEmail(), user.getRole().getName());

        // Return login response
        return new LoginResponse(
            token,
            "Bearer",
            user.getId(),
            user.getEmail(),
            user.getFullname(),
            user.getRole().getName()
        );
    }
}
