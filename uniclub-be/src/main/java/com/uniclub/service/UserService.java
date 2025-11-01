package com.uniclub.service;

import com.uniclub.dto.request.User.CreateUserRequest;
import com.uniclub.dto.request.User.UpdateUserRequest;
import com.uniclub.dto.response.User.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse createUser(CreateUserRequest request);
    UserResponse updateUser(Integer userId, UpdateUserRequest request);
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Integer userId);
    void deleteUser(Integer userId);
}