package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Login Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User Login Request")
public class UserLoginDTO {
    
    @NotBlank(message = "Email or username is required")
    @Schema(description = "Email or username", example = "john@example.com")
    private String usernameOrEmail;
    
    @NotBlank(message = "Password is required")
    @Schema(description = "User password")
    private String password;
}
