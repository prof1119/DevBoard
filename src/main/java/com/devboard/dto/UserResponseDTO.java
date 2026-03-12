package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * User Response DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User Response")
public class UserResponseDTO {
    
    @Schema(description = "User ID")
    private Long id;
    
    @Schema(description = "Username")
    private String username;
    
    @Schema(description = "Email address")
    private String email;
    
    @Schema(description = "First name")
    private String firstName;
    
    @Schema(description = "Last name")
    private String lastName;
    
    @Schema(description = "Profile picture URL")
    private String profilePicture;
    
    @Schema(description = "Is user active")
    private Boolean isActive;
    
    @Schema(description = "Is email verified")
    private Boolean emailVerified;
    
    @Schema(description = "Created at")
    private LocalDateTime createdAt;
    
    @Schema(description = "Updated at")
    private LocalDateTime updatedAt;
}
