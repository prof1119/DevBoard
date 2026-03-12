package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * JWT Authentication Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "JWT Authentication Response")
public class JwtAuthenticationResponse {
    
    @Schema(description = "JWT Access Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;
    
    @Schema(description = "Refresh Token")
    private String refreshToken;
    
    @Schema(description = "Token Type", example = "Bearer")
    private String tokenType = "Bearer";
    
    @Schema(description = "Token Expiration (in seconds)", example = "86400")
    private Long expiresIn;
    
    @Schema(description = "User Information")
    private UserResponseDTO user;
}
