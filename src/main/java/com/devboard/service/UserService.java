package com.devboard.service;


import com.devboard.dto.*;
import com.devboard.entity.User;
import com.devboard.exception.BadRequestException;
import com.devboard.exception.DuplicateResourceException;
import com.devboard.exception.ResourceNotFoundException;
import com.devboard.repository.UserRepository;
import com.devboard.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UserService - خدمة إدارة المستخدمين
 * تتعامل مع التسجيل والدخول وتحديث بيانات المستخدم
 */
@Service
@Transactional
@Slf4j
public class UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Autowired
    private UserMapper userMapper;
    
    /**
     * تسجيل مستخدم جديد
     */
    public UserResponseDTO registerUser(UserRegistrationDTO registrationDTO) {
        log.info("Registering new user: {}", registrationDTO.getUsername());
        
        // التحقق من عدم وجود مستخدم بنفس البيانات
        if (userRepository.existsByUsername(registrationDTO.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + registrationDTO.getUsername());
        }
        
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + registrationDTO.getEmail());
        }
        
        // التحقق من تطابق كلمتي المرور
        if (!registrationDTO.getPassword().equals(registrationDTO.getConfirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }
        
        // إنشاء مستخدم جديد
        User user = User.builder()
                .username(registrationDTO.getUsername())
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .firstName(registrationDTO.getFirstName())
                .lastName(registrationDTO.getLastName())
                .isActive(true)
                .emailVerified(false)
                .build();
        
        user = userRepository.save(user);
        log.info("User registered successfully: {}", user.getUsername());
        
        return userMapper.toUserResponseDTO(user);
    }
    
    /**
     * تسجيل الدخول
     */
    public JwtAuthenticationResponse loginUser(UserLoginDTO loginDTO) {
        log.info("User login attempt: {}", loginDTO.getUsernameOrEmail());
        
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getUsernameOrEmail(),
                            loginDTO.getPassword()
                    )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            String accessToken = jwtTokenProvider.generateAccessToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication.getName());
            
            User user = userRepository.findByUsernameOrEmail(
                    loginDTO.getUsernameOrEmail(),
                    loginDTO.getUsernameOrEmail()
            ).orElseThrow(() -> new ResourceNotFoundException("User not found"));
            
            log.info("User logged in successfully: {}", user.getUsername());
            
            return JwtAuthenticationResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .tokenType("Bearer")
                    .expiresIn(86400L)
                    .user(userMapper.toUserResponseDTO(user))
                    .build();
        } catch (Exception ex) {
            log.error("Authentication failed: {}", ex.getMessage());
            throw new BadRequestException("Invalid username or password");
        }
    }
    
    /**
     * الحصول على بيانات المستخدم الحالي
     */
    public UserResponseDTO getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Current user not found"));
        return userMapper.toUserResponseDTO(user);
    }
    
    /**
     * الحصول على بيانات مستخدم بالمعرف
     */
    public UserResponseDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return userMapper.toUserResponseDTO(user);
    }
    
    /**
     * تحديث بيانات المستخدم
     */
    public UserResponseDTO updateUser(Long userId, UserUpdateDTO updateDTO) {
        log.info("Updating user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        if (updateDTO.getFirstName() != null) {
            user.setFirstName(updateDTO.getFirstName());
        }
        
        if (updateDTO.getLastName() != null) {
            user.setLastName(updateDTO.getLastName());
        }
        
        if (updateDTO.getProfilePicture() != null) {
            user.setProfilePicture(updateDTO.getProfilePicture());
        }
        
        user = userRepository.save(user);
        log.info("User updated successfully: {}", user.getUsername());
        
        return userMapper.toUserResponseDTO(user);
    }
    
    /**
     * تغيير كلمة المرور
     */
    public void changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        log.info("Changing password for user: {}", userId);
        
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        
        // التحقق من كلمة المرور القديمة
        if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
            throw new BadRequestException("Old password is incorrect");
        }
        
        // التحقق من تطابق كلمتي المرور الجديدة
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new BadRequestException("New passwords do not match");
        }
        
        // تحديث كلمة المرور
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
        userRepository.save(user);
        
        log.info("Password changed successfully for user: {}", user.getUsername());
    }
    
    /**
     * البحث عن مستخدمين
     */
    public List<UserResponseDTO> searchUsers(String search) {
        List<User> users = userRepository.searchUsers(search);
        return users.stream()
                .map(userMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * الحصول على جميع المستخدمين النشطين
     */
    public List<UserResponseDTO> getAllActiveUsers() {
        List<User> users = userRepository.findAllActiveUsers();
        return users.stream()
                .map(userMapper::toUserResponseDTO)
                .collect(Collectors.toList());
    }
}
