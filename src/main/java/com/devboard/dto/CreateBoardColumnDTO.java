package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Create Board Column Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Create Board Column Request")
public class CreateBoardColumnDTO {
    
    @NotBlank(message = "Column name is required")
    @Size(min = 2, max = 50, message = "Column name must be between 2 and 50 characters")
    @Schema(description = "Column name", example = "In Progress")
    private String name;
    
    @Size(max = 255, message = "Description must not exceed 255 characters")
    @Schema(description = "Column description")
    private String description;
    
    @Schema(description = "Column position in board")
    private Integer position = 0;
}
