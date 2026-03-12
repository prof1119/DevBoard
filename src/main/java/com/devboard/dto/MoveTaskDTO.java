package com.devboard.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Move Task Request DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Move Task Request")
public class MoveTaskDTO {
    
    @NotNull(message = "Target column ID is required")
    @Schema(description = "Target column ID")
    private Long targetColumnId;
    
    @Schema(description = "New position in column")
    private Integer newPosition;
}
