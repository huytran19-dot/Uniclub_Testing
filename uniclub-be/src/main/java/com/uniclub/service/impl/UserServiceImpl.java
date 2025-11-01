package com.uniclub.service.impl;

import com.uniclub.dto.request.User.CreateUserRequest;
import com.uniclub.dto.request.Auth.LoginRequest;
import com.uniclub.dto.request.User.RegisterRequest;
import com.uniclub.dto.request.User.UpdateUserRequest;
import com.uniclub.dto.request.User.VerifyCodeRequest;
import com.uniclub.dto.response.Auth.LoginResponse;
import com.uniclub.dto.response.User.UserResponse;
import com.uniclub.entity.User;
import com.uniclub.entity.Role;
import com.uniclub.exception.ResourceNotFoundException;
import com.uniclub.repository.UserRepository;
import com.uniclub.repository.RoleRepository;
import com.uniclub.service.UserService;
import com.uniclub.service.VerificationService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private VerificationService verificationService;

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    public UserResponse getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return UserResponse.fromEntity(user);
    }

    @Override
    public UserResponse createUser(CreateUserRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        // Check if role exists
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException("Role", "id", request.getRoleId()));

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullname(request.getFullname());
        user.setRole(role);
        user.setStatus(request.getStatus() != null ? request.getStatus() : 1);

        User savedUser = userRepository.save(user);
        return UserResponse.fromEntity(savedUser);
    }

    @Override
    public UserResponse updateUser(Integer userId, UpdateUserRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Check if email already exists (excluding current user)
        if (!user.getEmail().equals(request.getEmail()) && 
            userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email đã tồn tại");
        }

        // Update fields
        user.setEmail(request.getEmail());
        user.setFullname(request.getFullname());
        user.setStatus(request.getStatus());

        // Update password only if provided
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        // Update role if provided
        if (request.getRoleId() != null) {
            Role role = roleRepository.findById(request.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role", "id", request.getRoleId()));
            user.setRole(role);
        }

        User updatedUser = userRepository.save(user);
        return UserResponse.fromEntity(updatedUser);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromEntity)
                .toList();
    }

    @Override
    public UserResponse getUserById(Integer userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        return UserResponse.fromEntity(user);
    }

    @Override
    public void deleteUser(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User", "id", userId);
        }
        userRepository.deleteById(userId);
    }

    @Override
    public void inactiveUser(Integer userId) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
        existingUser.setStatus((byte) 0);
        userRepository.save(existingUser);
    }

    @Override
    public UserResponse registerNewUser(RegisterRequest request) {
        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already registered. Please login or use a different email.");
        }

        // Encode password
        String encodedPassword = passwordEncoder.encode(request.getPassword());

        // Find or create USER role
        Role userRole = roleRepository.findById(1).orElseGet(() -> {
            // If role doesn't exist, create it
            Role newRole = new Role();
            newRole.setName("USER");
            newRole.setDescription("Regular user/customer role");
            newRole.setStatus((byte) 1);
            return roleRepository.save(newRole);
        });

        // Create new user with INACTIVE status (0)
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(encodedPassword);
        user.setFullname(request.getFullname());
        user.setRole(userRole);
        user.setStatus((byte) 0); // Inactive/unverified status

        // Save user
        User savedUser = userRepository.save(user);

        // Generate and store verification code in memory
        verificationService.generateAndStoreCode(savedUser.getEmail());

        return UserResponse.fromEntity(savedUser);
    }

    @Override
    public boolean verifyUser(String token) {
        // Legacy method - not used in code-based verification
        // Kept for compatibility
        return false;
    }

    @Override
    public boolean verifyUserByCode(VerifyCodeRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if already verified (status 2 = verified user)
        if (user.getStatus() == 2) {
            throw new IllegalArgumentException("Account is already verified");
        }

        boolean isCodeValid = verificationService.verifyCode(request.getEmail(), request.getCode());

        if (!isCodeValid) {
            throw new IllegalArgumentException("Invalid or expired verification code");
        }

        // Activate account as verified user (status 2)
        user.setStatus((byte) 2);
        // Save updated user
        userRepository.save(user);

        return true;
    }

    @Override
    public void resendVerificationCode(String email) {
        // Find user by email
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Check if already verified (status 2 = verified user)
        if (user.getStatus() == 2) {
            throw new IllegalArgumentException("Account is already verified");
        }
        // Generate and send new code
        verificationService.generateAndStoreCode(email);
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        // Find user by email
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Invalid email or password"));

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password");
        }

        // Check if account is verified (status = 1)
        if (user.getStatus() != 1) {
            throw new IllegalStateException("Account is not verified. Please check your email to verify your account.");
        }

        // Return user data
        LoginResponse response = new LoginResponse();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullname());
        response.setRole(user.getRole().getName());
        response.setType("Bearer");

        return response;
    }
}
