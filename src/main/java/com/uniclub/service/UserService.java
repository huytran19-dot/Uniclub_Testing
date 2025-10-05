package com.uniclub.service;

import com.uniclub.dto.request.User.CreateUserRequest;
import com.uniclub.dto.request.User.UpdateUserRequest;
import com.uniclub.dto.response.User.UserResponse;

import java.util.List;

public interface UserService {
    List<UserResponse> getAllUsers();
    UserResponse getUserById(Integer userId);

    UserResponse createUser(CreateUserRequest request);
    UserResponse updateUser(Integer userId, UpdateUserRequest request);
    void deleteUser(Integer userId);
    void inactiveUser(Integer userId);
}
