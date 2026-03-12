package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Change Password Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Change Password Request")
public class ChangePasswordDTO {
    
    @NotBlank(message = "Old password is required")
    @Schema(description = "Current password")
    private String oldPassword;
    
    @NotBlank(message = "New password is required")
    @Size(min = 8, max = 255, message = "Password must be between 8 and 255 characters")
    @Schema(description = "New password")
    private String newPassword;
    
    @NotBlank(message = "Confirm password is required")
    @Schema(description = "Confirm new password")
    private String confirmPassword;
}
