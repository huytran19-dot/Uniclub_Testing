package com.uniclub.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.uniclub.config.JwtConfig;
import com.uniclub.dto.request.Auth.LoginRequest;
import com.uniclub.dto.response.Auth.LoginResponse;
import com.uniclub.entity.User;
import com.uniclub.exception.UnverifiedAccountException;
import com.uniclub.repository.UserRepository;

/**
 * Authentication Service
 * 
 * User Status (trạng thái tài khoản):
 * - 0: Unverified/Inactive (chưa xác thực email)
 * - 1: Active/Verified (đã xác thực email - user thông thường)
 * - 2: Banned/Disabled (bị khóa tài khoản)
 * 
 * User Role (vai trò - lưu ở bảng role):
 * - 1: SysAdmin (Quản trị viên)
 * - 2: Buyer (Người mua)
 */
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
            throw new RuntimeException("Email hoặc mật khẩu không đúng");
        }

        User user = userOptional.get();

        // Check user status
        // 0 = unverified email, 1 = admin, 2 = verified user
        if (user.getStatus() == 0) {
            // ✅ Throw special exception with email for verification resend
            throw new UnverifiedAccountException(
                user.getEmail(),
                "Tài khoản chưa được xác thực. Mã xác thực mới đã được gửi đến email của bạn."
            );
        }
        
        // Only admin (1) and verified users (2) can login
        if (user.getStatus() != 1 && user.getStatus() != 2) {
            throw new RuntimeException("Tài khoản không hợp lệ hoặc đã bị khóa.");
        }

        // Verify password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new RuntimeException("Email hoặc mật khẩu không đúng");
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
