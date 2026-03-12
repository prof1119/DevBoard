package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login Request DTO (Alternative name for UserLoginDTO)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Login Request")
public class LoginRequest {
    
    @NotBlank(message = "Email or username is required")
    @Schema(description = "Email or username", example = "john@example.com")
    private String usernameOrEmail;
    
    @NotBlank(message = "Password is required")
    @Schema(description = "User password")
    private String password;
}
