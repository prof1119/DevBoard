package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create Board Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Create Board Request")
public class CreateBoardDTO {
    
    @NotBlank(message = "Board name is required")
    @Size(min = 2, max = 100, message = "Board name must be between 2 and 100 characters")
    @Schema(description = "Board name", example = "Q4 Product Launch")
    private String name;
    
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Schema(description = "Board description")
    private String description;
    
    @Schema(description = "Board background image URL")
    private String backgroundImage;
    
    @Schema(description = "Is board public", example = "false")
    private Boolean isPublic = false;
}
