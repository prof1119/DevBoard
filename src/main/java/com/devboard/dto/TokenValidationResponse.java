package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Token Validation Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Token Validation Response")
public class TokenValidationResponse {
    
    @Schema(description = "Is token valid")
    private Boolean isValid;
    
    @Schema(description = "Username extracted from token")
    private String username;
    
    @Schema(description = "Token expiration time")
    private Long expiresIn;
}
