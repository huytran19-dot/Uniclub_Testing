package com.uniclub.controller;

import com.uniclub.dto.request.User.CreateUserRequest;
import com.uniclub.dto.request.User.UpdateUserRequest;
import com.uniclub.dto.response.User.UserResponse;
import com.uniclub.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Lấy danh sách tất cả user
    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Lấy user theo ID
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable("userId") Integer userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    // Tạo user mới
    @PostMapping
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.ok(response);
    }

    // Cập nhật user
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable("userId") Integer userId,
            @Valid @RequestBody UpdateUserRequest request) {
        UserResponse updatedUser = userService.updateUser(userId, request);
        return ResponseEntity.ok(updatedUser);
    }

    // Xóa user
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    // Inactivate user (soft delete)
    @PatchMapping("/{userId}/inactive")
    public ResponseEntity<Void> inactiveUser(@PathVariable Integer userId) {
        userService.inactiveUser(userId);
        return ResponseEntity.noContent().build();
    }
}
