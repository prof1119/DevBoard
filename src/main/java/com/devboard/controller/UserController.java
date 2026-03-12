package com.devboard.controller;


import com.devboard.dto.ChangePasswordDTO;
import com.devboard.dto.UserResponseDTO;
import com.devboard.dto.UserUpdateDTO;
import com.devboard.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * UserController - متحكم المستخدمين
 */
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "User management endpoints")
@SecurityRequirement(name = "Bearer Token")
@Slf4j
public class UserController {
    
    @Autowired
    private UserService userService;
    
    /**
     * الحصول على بيانات المستخدم الحالي
     */
    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<UserResponseDTO> getCurrentUser() {
        log.info("Getting current user profile");
        UserResponseDTO user = userService.getCurrentUser();
        return ResponseEntity.ok(user);
    }
    
    /**
     * الحصول على بيانات مستخدم معين
     */
    @GetMapping("/{userId}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long userId) {
        log.info("Getting user with ID: {}", userId);
        UserResponseDTO user = userService.getUserById(userId);
        return ResponseEntity.ok(user);
    }
    
    /**
     * تحديث بيانات المستخدم
     */
    @PutMapping("/{userId}")
    @Operation(summary = "Update user profile")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long userId,
            @Valid @RequestBody UserUpdateDTO updateDTO) {
        log.info("Updating user with ID: {}", userId);
        UserResponseDTO user = userService.updateUser(userId, updateDTO);
        return ResponseEntity.ok(user);
    }
    
    /**
     * تغيير كلمة المرور
     */
    @PostMapping("/{userId}/change-password")
    @Operation(summary = "Change user password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long userId,
            @Valid @RequestBody ChangePasswordDTO changePasswordDTO) {
        log.info("Changing password for user: {}", userId);
        userService.changePassword(userId, changePasswordDTO);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * البحث عن مستخدمين
     */
    @GetMapping("/search")
    @Operation(summary = "Search users")
    public ResponseEntity<List<UserResponseDTO>> searchUsers(@RequestParam String search) {
        log.info("Searching users with query: {}", search);
        List<UserResponseDTO> users = userService.searchUsers(search);
        return ResponseEntity.ok(users);
    }
    
    /**
     * الحصول على جميع المستخدمين النشطين
     */
    @GetMapping
    @Operation(summary = "Get all active users")
    public ResponseEntity<List<UserResponseDTO>> getAllActiveUsers() {
        log.info("Getting all active users");
        List<UserResponseDTO> users = userService.getAllActiveUsers();
        return ResponseEntity.ok(users);
    }
}
