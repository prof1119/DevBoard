package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * User Update Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "User Update Request")
public class UserUpdateDTO {
    
    @Size(min = 2, max = 100, message = "First name must be between 2 and 100 characters")
    @Schema(description = "First name")
    private String firstName;
    
    @Size(min = 2, max = 100, message = "Last name must be between 2 and 100 characters")
    @Schema(description = "Last name")
    private String lastName;
    
    @Schema(description = "Profile picture URL")
    private String profilePicture;
}
