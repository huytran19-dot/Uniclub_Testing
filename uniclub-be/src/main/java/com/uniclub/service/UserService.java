package com.uniclub.service;

import com.uniclub.dto.request.User.CreateUserRequest;
import com.uniclub.dto.request.Auth.LoginRequest;
import com.uniclub.dto.request.User.RegisterRequest;
import com.uniclub.dto.request.User.UpdateUserRequest;
import com.uniclub.dto.request.User.VerifyCodeRequest;
import com.uniclub.dto.response.Auth.LoginResponse;
import com.uniclub.dto.response.User.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Integer userId);

    UserResponse createUser(CreateUserRequest request);
    UserResponse updateUser(Integer userId, UpdateUserRequest request);
    void deleteUser(Integer userId);
    void inactiveUser(Integer userId);
    
//     New methods for registration and verification
    UserResponse registerNewUser(RegisterRequest request);
    boolean verifyUser(String token);
    boolean verifyUserByCode(VerifyCodeRequest request);
    void resendVerificationCode(String email);
    LoginResponse loginUser(LoginRequest request);
}
