package com.devboard.controller;


import com.devboard.dto.*;
import com.devboard.security.JwtTokenProvider;
import com.devboard.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AuthController - متحكم المصادقة والتسجيل
 */
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "User authentication endpoints")
@Slf4j
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    /**
     * تسجيل مستخدم جديد
     */
    @PostMapping("/register")
    @Operation(summary = "Register a new user")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody UserRegistrationDTO registrationDTO) {
        log.info("Registering new user: {}", registrationDTO.getUsername());
        UserResponseDTO user = userService.registerUser(registrationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }
    
    /**
     * تسجيل الدخول
     */
    @PostMapping("/login")
    @Operation(summary = "Login user")
    public ResponseEntity<JwtAuthenticationResponse> login(@Valid @RequestBody UserLoginDTO loginDTO) {
        log.info("User login: {}", loginDTO.getUsernameOrEmail());
        JwtAuthenticationResponse response = userService.loginUser(loginDTO);
        return ResponseEntity.ok(response);
    }
    
    /**
     * التحقق من صحة التوكن
     */
    @GetMapping("/validate")
    @Operation(summary = "Validate JWT token")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            boolean isValid = jwtTokenProvider.validateToken(jwt);
            return ResponseEntity.ok(isValid);
        }
        return ResponseEntity.ok(false);
    }
    
    /**
     * تحديث التوكن
     */
    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<JwtAuthenticationResponse> refreshToken(@RequestBody RefreshTokenRequest request) {
        String username = jwtTokenProvider.getUsernameFromToken(request.getRefreshToken());
        if (username != null && jwtTokenProvider.validateToken(request.getRefreshToken())) {
            String newAccessToken = jwtTokenProvider.generateAccessToken(username);
            UserResponseDTO user = userService.getUserById(extractUserIdFromUsername(username));
            
            JwtAuthenticationResponse response = JwtAuthenticationResponse.builder()
                    .accessToken(newAccessToken)
                    .tokenType("Bearer")
                    .expiresIn(86400L)
                    .user(user)
                    .build();
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    /**
     * طريقة مساعدة لاستخراج معرف المستخدم من اسم المستخدم
     * (يجب استبدالها بطريقة أفضل في التطبيق الفعلي)
     */
    private Long extractUserIdFromUsername(String username) {
        UserResponseDTO user = userService.getUserById(1L); // مثال فقط
        return user.getId();
    }
}
