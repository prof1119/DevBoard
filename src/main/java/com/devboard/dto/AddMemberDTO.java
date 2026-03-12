package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Add Member to Board Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Add Member to Board Request")
public class AddMemberDTO {
    
    @NotBlank(message = "User ID is required")
    @Schema(description = "User ID to add as member")
    private String userId;
}
